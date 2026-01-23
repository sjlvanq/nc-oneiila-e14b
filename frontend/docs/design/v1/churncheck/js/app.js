/* ====== CLIENTES SIMULADOS (ÚNICA FUENTE DE DATOS) ====== */
const clientes = [
  {
    id: 1,
    dni: "87654321",
    nombre: "Carlos Méndez",
    edad: 42,
    sexo: "Masculino",
    asistencia: 32,
    estatura: "1.74 m",
    peso: "92 kg",
    contrato: "20/02/2026"
  },
  {
    id: 2,
    dni: "12345678",
    nombre: "Ana López",
    edad: 29,
    sexo: "Femenino",
    asistencia: 78,
    estatura: "1.62 m",
    peso: "60 kg",
    contrato: "15/11/2026"
  },
  {
    id: 3,
    dni: "99887766",
    nombre: "Luis Ramírez",
    edad: 35,
    sexo: "Masculino",
    asistencia: 55,
    estatura: "1.70 m",
    peso: "78 kg",
    contrato: "01/08/2026"
  }
];

/* ====== IA SIMULADA ====== */
function calcularRiesgo(c) {
  if (c.asistencia < 35) return { nivel: "alto", porcentaje: 87 };
  if (c.asistencia < 60) return { nivel: "medio", porcentaje: 45 };
  return { nivel: "bajo", porcentaje: 12 };
}

function mensajeIA(cliente) {
  const r = calcularRiesgo(cliente);

  if (r.nivel === "alto") {
    return {
      clase: "alerta-alta",
      texto: `⚠️ Predicción IA: Riesgo ALTO de abandono (${r.porcentaje}%). 
      Motivo: baja asistencia (${cliente.asistencia}%) + contrato próximo a vencer.`
    };
  }

  if (r.nivel === "medio") {
    return {
      clase: "alerta-media",
      texto: `⚠️ Predicción IA: Riesgo MEDIO (${r.porcentaje}%). 
      Motivo: asistencia irregular (${cliente.asistencia}%).`
    };
  }

  return {
    clase: "alerta-baja",
    texto: `✅ Predicción IA: Riesgo BAJO (${r.porcentaje}%). 
    Cliente estable con buena asistencia (${cliente.asistencia}%).`
  };
}

/* ====== LISTADO POR RIESGO ====== */
function verListado(tipo) {
  document.getElementById("dashboard").classList.add("hidden");
  document.getElementById("listado").classList.remove("hidden");

  const tabla = document.getElementById("tabla");
  tabla.innerHTML = "";

  clientes
    .filter(c => calcularRiesgo(c).nivel === tipo)
    .forEach(c => {
      tabla.innerHTML += `
        <tr>
          <td>${c.nombre}</td>
          <td>${c.dni}</td>
          <td>${c.asistencia}%</td>
          <td><span class="badge ${tipo}">${tipo.toUpperCase()}</span></td>
          <td>
            <a href="perfil.html?id=${c.id}" class="action">Ver ficha</a>
          </td>
        </tr>`;
    });
}

function volver() {
  document.getElementById("listado").classList.add("hidden");
  document.getElementById("dashboard").classList.remove("hidden");
}

/* ====== PERFIL DEL CLIENTE ====== */
function cargarPerfilClientePorId(id) {
  const cliente = clientes.find(c => c.id == id);
  if (!cliente) return;

  const riesgo = calcularRiesgo(cliente);
  const ia = mensajeIA(cliente);

  document.getElementById("nombreCliente").innerText = cliente.nombre;
  document.getElementById("dniCliente").innerText = cliente.dni;
  document.getElementById("edadCliente").innerText = cliente.edad + " años";
  document.getElementById("sexoCliente").innerText = cliente.sexo;
  document.getElementById("asistenciaCliente").innerText = cliente.asistencia + "%";
  document.getElementById("estaturaCliente").innerText = cliente.estatura;
  document.getElementById("pesoCliente").innerText = cliente.peso;
  document.getElementById("contratoCliente").innerText = cliente.contrato;

  // Imagen por sexo
  document.getElementById("fotoCliente").src =
    cliente.sexo === "Masculino"
      ? "img/man1.png"
      : "img/woman1.png";

  // Mensaje IA
  const alerta = document.getElementById("alertaIA");
  alerta.className = "alerta " + ia.clase;
  alerta.innerText = ia.texto;
}

/* ====== CARGA AUTOMÁTICA POR URL ====== */
document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");
  if (id) cargarPerfilClientePorId(id);
});
