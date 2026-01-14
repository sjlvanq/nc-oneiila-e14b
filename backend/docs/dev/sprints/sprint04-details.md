# Sprint 4 - Detalles de Implementación

Este documento contiene las instrucciones detalladas, código de ejemplo y configuraciones necesarias para completar el [Sprint 4](sprint04.md).

Ha sido generado automáticamente con Claude Sonet 4.5. Sólo para su uso como guía y referencia.

---

## Tabla de Contenidos

1. [Dev A: Despliegue y CI/CD](#dev-a-despliegue-y-cicd)
2. [Dev B: Caché de Predicciones](#dev-b-caché-de-predicciones)

---

## Dev A: Despliegue y CI/CD

### 1. Preparación del Entorno en OCI

#### Instalación de Dependencias
```bash
# Actualizar sistema
sudo apt update && sudo apt upgrade -y

# Java 17 (OpenJDK)
sudo apt install -y openjdk-17-jdk

# Nginx (reverse proxy)
sudo apt install -y nginx

# Git (para clonación de repositorio)
sudo apt install -y git

# Herramientas adicionales
sudo apt install -y curl wget unzip

# Verificar instalación de Java
java -version
# Debe mostrar: openjdk version "17.x.x"
```

#### Estructura de Directorios
```bash
# Crear directorios de aplicación
sudo mkdir -p /opt/churncheck
sudo mkdir -p /opt/churncheck/data  # Para la base de datos H2
sudo mkdir -p /opt/churncheck/backups
sudo mkdir -p /var/log/churncheck

# Asignar permisos al usuario backend
sudo chown -R backend:backend /opt/churncheck
sudo chown -R backend:backend /var/log/churncheck
```

#### Configuración de Nginx

Crear el archivo `/etc/nginx/sites-available/churncheck`:
```nginx
server {
    listen 80;
    server_name your-instance-ip;

    # Limitar tamaño de body para uploads
    client_max_body_size 10M;

    # Rate limiting para prevenir DDoS
    limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;
    limit_req zone=api_limit burst=20 nodelay;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
```

Habilitar la configuración:
```bash
# Crear enlace simbólico
sudo ln -s /etc/nginx/sites-available/churncheck /etc/nginx/sites-enabled/

# Verificar configuración
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx

# Habilitar inicio automático
sudo systemctl enable nginx
```

---

### 2. Configuración de H2 en Modo File

Crear el archivo `src/main/resources/application-production.yaml`:
```yaml
spring:
  application:
    name: churncheck-api
  datasource:
    url: jdbc:h2:file:/opt/churncheck/data/churncheck;AUTO_SERVER=TRUE
    driverClassName: org.h2.Driver
    username: sa
    password: ${H2_PASSWORD:}
  h2:
    console:
      enabled: false  # Deshabilitado en producción por seguridad
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  sql:
    init:
      mode: always

external:
  prediction:
    host: ${ML_SERVICE_HOST:https://lode.uno}
    endpoint: ${ML_SERVICE_ENDPOINT:/churncheck.php}
    api-key: ${ML_SERVICE_API_KEY}
    connect-timeout: 5000
    connection-timeout: 5000
    read-timeout: 10000
    max-retries: 3

logging:
  level:
    root: INFO
    com.churncheck.api: DEBUG
  file:
    name: /var/log/churncheck/application.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

**Notas sobre H2:**

- `AUTO_SERVER=TRUE` permite múltiples conexiones al mismo archivo.
- El archivo se persiste en `/opt/churncheck/data/churncheck.mv.db`.
- Para respaldos, simplemente copiar este archivo.

---

### 3. Pipeline de GitHub Actions

Crear el archivo `.github/workflows/deploy.yml`:
```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'
          
      - name: Build and Test
        run: mvn clean verify
        
      - name: Upload coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./target/site/jacoco/jacoco.xml
        continue-on-error: true

  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'
          
      - name: Build JAR
        run: mvn clean package -DskipTests
        
      - name: Deploy to OCI
        env:
          SSH_PRIVATE_KEY: ${{ secrets.OCI_SSH_KEY }}
          OCI_HOST: ${{ secrets.OCI_HOST }}
          OCI_USER: ${{ secrets.OCI_USER }}
        run: |
          echo "$SSH_PRIVATE_KEY" > deploy_key
          chmod 600 deploy_key
          
          # Copiar JAR al servidor
          scp -i deploy_key -o StrictHostKeyChecking=no \
            target/*.jar $OCI_USER@$OCI_HOST:/home/$OCI_USER/app.jar
          
          # Copiar configuración de producción
          scp -i deploy_key -o StrictHostKeyChecking=no \
            src/main/resources/application-production.yaml \
            $OCI_USER@$OCI_HOST:/home/$OCI_USER/application-production.yaml
          
          # Desplegar y reiniciar servicio
          ssh -i deploy_key -o StrictHostKeyChecking=no \
            $OCI_USER@$OCI_HOST << 'EOF'
            sudo systemctl stop churncheck
            sudo mv /home/$OCI_USER/app.jar /opt/churncheck/app.jar
            sudo mv /home/$OCI_USER/application-production.yaml /opt/churncheck/application-production.yaml
            sudo systemctl start churncheck
            sleep 5
            sudo systemctl status churncheck
          EOF
          
          rm deploy_key
```

---

### 4. Servicio systemd

Crear el archivo `/etc/systemd/system/churncheck.service`:
```ini
[Unit]
Description=ChurnCheck API
After=network.target

[Service]
Type=simple
User=backend
WorkingDirectory=/opt/churncheck
ExecStart=/usr/bin/java -jar /opt/churncheck/app.jar \
  --spring.profiles.active=production \
  --spring.config.location=/opt/churncheck/application-production.yaml
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal

# Variables de entorno opcionales
Environment="JAVA_OPTS=-Xmx512m -Xms256m"

[Install]
WantedBy=multi-user.target
```

Comandos para habilitar el servicio:
```bash
# Recargar configuración de systemd
sudo systemctl daemon-reload

# Habilitar inicio automático
sudo systemctl enable churncheck

# Iniciar servicio
sudo systemctl start churncheck

# Verificar estado
sudo systemctl status churncheck

# Ver logs en tiempo real
sudo journalctl -u churncheck -f
```

---

### 5. Script de Backup de Base de Datos

Crear el archivo `/opt/churncheck/backup.sh`:
```bash
#!/bin/bash

# Directorio de backups
BACKUP_DIR="/opt/churncheck/backups"
mkdir -p $BACKUP_DIR

# Nombre del backup con fecha
BACKUP_NAME="churncheck_$(date +%Y%m%d_%H%M%S).mv.db"

# Copiar archivo de base de datos
cp /opt/churncheck/data/churncheck.mv.db "$BACKUP_DIR/$BACKUP_NAME"

# Comprimir backup para ahorrar espacio
gzip "$BACKUP_DIR/$BACKUP_NAME"

# Mantener solo los últimos 7 backups
ls -t $BACKUP_DIR/*.mv.db.gz | tail -n +8 | xargs rm -f

echo "Backup completado: $BACKUP_NAME.gz"
```

Hacer el script ejecutable:
```bash
chmod +x /opt/churncheck/backup.sh
```

Configurar cron para backups diarios:
```bash
# Editar crontab del usuario backend
crontab -e

# Añadir línea para backup diario a las 2 AM
0 2 * * * /opt/churncheck/backup.sh >> /var/log/churncheck/backup.log 2>&1
```

---

### 6. Configuración de Secrets en GitHub

En GitHub, ir a: `Settings > Secrets and variables > Actions > New repository secret`

Crear los siguientes secrets:

- **OCI_SSH_KEY**: Contenido de la clave privada SSH (archivo completo)
- **OCI_HOST**: IP pública de la instancia (ej. `132.145.123.45`)
- **OCI_USER**: Usuario SSH (ej. `backend`)
- **ML_SERVICE_API_KEY**: API Key del servicio ML (si es necesaria)

---

### 7. Comandos Útiles para Troubleshooting
```bash
# Ver logs de la aplicación
sudo journalctl -u churncheck -n 100 --no-pager

# Ver logs en tiempo real
sudo journalctl -u churncheck -f

# Reiniciar aplicación
sudo systemctl restart churncheck

# Ver estado de Nginx
sudo systemctl status nginx

# Ver logs de Nginx
sudo tail -f /var/log/nginx/error.log
sudo tail -f /var/log/nginx/access.log

# Verificar puerto 8080 en uso
sudo netstat -tlnp | grep 8080

# Verificar espacio en disco
df -h

# Ver tamaño de base de datos
du -h /opt/churncheck/data/
```

---

## Dev B: Caché de Predicciones

### 1. Extensión de la Entidad Client

Agregar los siguientes campos a `Client.java`:
```java
@Column(name = "last_prediction_churn")
private Byte lastPredictionChurn;

@Column(name = "last_prediction_probability")
private Double lastPredictionProbability;

@Column(name = "last_prediction_timestamp")
private Instant lastPredictionTimestamp;

// Getters y setters
public Byte getLastPredictionChurn() { 
    return lastPredictionChurn; 
}

public void setLastPredictionChurn(Byte churn) { 
    this.lastPredictionChurn = churn; 
}

public Double getLastPredictionProbability() { 
    return lastPredictionProbability; 
}

public void setLastPredictionProbability(Double probability) { 
    this.lastPredictionProbability = probability; 
}

public Instant getLastPredictionTimestamp() { 
    return lastPredictionTimestamp; 
}

public void setLastPredictionTimestamp(Instant timestamp) { 
    this.lastPredictionTimestamp = timestamp; 
}
```

**Nota:** Como se usa `ddl-auto: update`, H2 creará automáticamente estas columnas al iniciar la aplicación.

---

### 2. Lógica de Caché en ChurnService

Modificar `ChurnService.java`:
```java
package com.churncheck.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.clients.PredictionClient;

import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.Instant;

@Service
public class ChurnService {

    private static final Logger logger = LoggerFactory.getLogger(ChurnService.class);
    
    private final PredictionClient predictionClient;
    private final ClientPredictionMapper mapper;
    private final ClientRepository clientRepository;

    // TTL: 24 horas
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    public ChurnService(PredictionClient predictionClient, 
                        ClientPredictionMapper mapper,
                        ClientRepository clientRepository) {
        this.predictionClient = predictionClient;
        this.mapper = mapper;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public PredictionResponseDTO predict(Client client) {
        // Verificar si existe caché válido
        if (isCacheValid(client)) {
            logger.info("Returning cached prediction for client {} (cached at: {})", 
                client.getId(), client.getLastPredictionTimestamp());
            return new PredictionResponseDTO(
                client.getLastPredictionChurn(),
                client.getLastPredictionProbability(),
                client.getLastPredictionTimestamp()
            );
        }

        // Llamar al microservicio
        logger.info("Fetching new prediction for client {} (cache miss or expired)", 
            client.getId());
        PredictionRequestDTO request = mapper.toPredictionRequest(client);
        PredictionResponseDTO response = predictionClient.predict(request);

        // Guardar en caché
        client.setLastPredictionChurn(response.churn());
        client.setLastPredictionProbability(response.probability());
        client.setLastPredictionTimestamp(response.timestamp());
        clientRepository.save(client);
        
        logger.info("Prediction cached for client {}: churn={}, probability={}", 
            client.getId(), response.churn(), response.probability());

        return response;
    }

    private boolean isCacheValid(Client client) {
        if (client.getLastPredictionTimestamp() == null) {
            return false;
        }
        
        Instant now = Instant.now();
        Instant cacheExpiry = client.getLastPredictionTimestamp().plus(CACHE_TTL);
        
        return now.isBefore(cacheExpiry);
    }

    @Transactional
    public void invalidateCache(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        
        logger.info("Invalidating cache for client {}", clientId);
        
        client.setLastPredictionChurn(null);
        client.setLastPredictionProbability(null);
        client.setLastPredictionTimestamp(null);
        
        clientRepository.save(client);
        
        logger.info("Cache invalidated successfully for client {}", clientId);
    }
}
```

---

### 3. Endpoint de Invalidación (Extra)

Agregar en `ClientController.java`:
```java
@Operation(summary = "Invalidate prediction cache", 
           description = "Forces recalculation of churn prediction on next request")
@ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Cache invalidated successfully"),
    @ApiResponse(responseCode = "404", description = "Client not found",
        content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
})
@DeleteMapping("/{id}/prediction-cache")
@Transactional
public ResponseEntity<Void> invalidatePredictionCache(@PathVariable Long id) {
    churnService.invalidateCache(id);
    return ResponseEntity.noContent().build();
}
```

---

### 4. Tests para Caché

Crear `ChurnServiceCacheTest.java`:
```java
package com.churncheck.api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.clients.PredictionClient;
import com.churncheck.api.service.ChurnService;

@ExtendWith(MockitoExtension.class)
class ChurnServiceCacheTest {

    @Mock
    private PredictionClient predictionClient;

    @Mock
    private ClientPredictionMapper mapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ChurnService churnService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
    }

    @Test
    void shouldUseCacheWhenValid() {
        // Given: Cliente con caché válido (hace 1 hora)
        client.setLastPredictionChurn((byte) 1);
        client.setLastPredictionProbability(0.85);
        client.setLastPredictionTimestamp(Instant.now().minus(1, ChronoUnit.HOURS));

        // When
        PredictionResponseDTO result = churnService.predict(client);

        // Then: No debe llamar al microservicio
        verify(predictionClient, never()).predict(any());
        assertEquals((byte) 1, result.churn());
        assertEquals(0.85, result.probability());
    }

    @Test
    void shouldCallMicroserviceWhenCacheExpired() {
        // Given: Cliente con caché expirado (hace 25 horas)
        client.setLastPredictionChurn((byte) 0);
        client.setLastPredictionProbability(0.25);
        client.setLastPredictionTimestamp(Instant.now().minus(25, ChronoUnit.HOURS));

        PredictionResponseDTO newPrediction = new PredictionResponseDTO(
            (byte) 1, 0.92, Instant.now()
        );

        when(mapper.toPredictionRequest(client)).thenReturn(mock(PredictionRequestDTO.class));
        when(predictionClient.predict(any())).thenReturn(newPrediction);
        when(clientRepository.save(any())).thenReturn(client);

        // When
        PredictionResponseDTO result = churnService.predict(client);

        // Then: Debe llamar al microservicio
        verify(predictionClient, times(1)).predict(any());
        assertEquals((byte) 1, result.churn());
        assertEquals(0.92, result.probability());
    }

    @Test
    void shouldCallMicroserviceWhenNoCacheExists() {
        // Given: Cliente sin caché
        client.setLastPredictionTimestamp(null);

        PredictionResponseDTO newPrediction = new PredictionResponseDTO(
            (byte) 1, 0.75, Instant.now()
        );

        when(mapper.toPredictionRequest(client)).thenReturn(mock(PredictionRequestDTO.class));
        when(predictionClient.predict(any())).thenReturn(newPrediction);
        when(clientRepository.save(any())).thenReturn(client);

        // When
        PredictionResponseDTO
	result = churnService.predict(client);

        // Then: Debe llamar al microservicio
        verify(predictionClient, times(1)).predict(any());
        verify(clientRepository, times(1)).save(client);
    }
}

---

## Dev C: Endpoints Analíticos

> En revisión por discrepancias con el equipo de Front-End


## Checklist de Validación

### Dev A

- [ ] Java 17 instalado y funcionando
- [ ] Nginx configurado y proxy funcionando
- [ ] Servicio systemd creado y habilitado
- [ ] Pipeline de GitHub Actions ejecutándose correctamente
- [ ] Script de backup configurado en cron
- [ ] Base de datos H2 persistiendo correctamente en `/opt/churncheck/data/`
- [ ] Logs visibles en `/var/log/churncheck/`

### Dev B

- [ ] Campos de caché agregados a `Client.java`
- [ ] Lógica de caché implementada en `ChurnService`
- [ ] TTL de 24 horas funcionando correctamente
- [ ] Endpoint de invalidación creado y documentado
- [ ] Tests unitarios de caché pasando
- [ ] Logs indicando uso de caché vs llamadas nuevas

---

## Fin del Sprint 4

Este documento contiene información técnica necesaria para completar el Sprint 4. Para cualquier duda sobre el alcance general, consultar [sprint04.md](sprint04.md).
