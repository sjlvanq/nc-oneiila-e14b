<!-- Generator: Widdershins v4.0.1 -->

<h1 id="openapi-definition">OpenAPI definition v0</h1>

> Scroll down for code samples, example requests and responses. Select a language for code samples from the tabs above or the mobile navigation menu.

Base URLs:

* <a href="http://localhost:8080">http://localhost:8080</a>

# Authentication

- HTTP Authentication, scheme: bearer 

<h1 id="openapi-definition-clients">Clients</h1>

Client management and churn prediction endpoints

## List active clients

<a id="opIdgetClientList"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/clients?pageable=page,0,size,1,sort,string \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript

const headers = {
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/clients?pageable=page,0,size,1,sort,string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`GET /clients`

<h3 id="list-active-clients-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|pageable|query|[Pageable](#schemapageable)|true|none|

> Example responses

> 200 Response

```json
{
  "totalElements": 0,
  "totalPages": 0,
  "pageable": {
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0,
    "offset": 0,
    "sort": {
      "sorted": true,
      "empty": true,
      "unsorted": true
    },
    "unpaged": true
  },
  "size": 0,
  "content": [
    {
      "id": 0,
      "clientName": "string",
      "active": true,
      "gender": "MALE",
      "clientPhone": "string",
      "nearLocation": true,
      "age": 0
    }
  ],
  "number": 0,
  "sort": {
    "sorted": true,
    "empty": true,
    "unsorted": true
  },
  "first": true,
  "last": true,
  "numberOfElements": 0,
  "empty": true
}
```

<h3 id="list-active-clients-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Clients retrieved successfully|[PageClientListResponseDTO](#schemapageclientlistresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid pagination or sorting parameters|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Missing or invalid JWT token|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

## Update client

<a id="opIdupdateClient"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:8080/clients \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript
const inputBody = '{
  "id": 1,
  "clientName": "string",
  "clientPhone": "string",
  "nearLocation": true,
  "birthDate": "2019-08-24"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/clients',
{
  method: 'PUT',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`PUT /clients`

> Body parameter

```json
{
  "id": 1,
  "clientName": "string",
  "clientPhone": "string",
  "nearLocation": true,
  "birthDate": "2019-08-24"
}
```

<h3 id="update-client-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[ClientUpdateRequestDTO](#schemaclientupdaterequestdto)|true|none|

> Example responses

> 200 Response

```json
{
  "id": 0,
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "clientPhone": "string",
  "nearLocation": true,
  "age": 0
}
```

<h3 id="update-client-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Client updated successfully|[ClientResponseDTO](#schemaclientresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Validation error|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Missing or invalid JWT token|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Client not found|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

## Create client

<a id="opIdcreateClient"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/clients \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript
const inputBody = '{
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "nearLocation": true,
  "partnerId": 0,
  "promoFriends": true,
  "clientPhone": "string",
  "birthDate": "2019-08-24",
  "contractPeriod": 1,
  "groupVisits": true
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/clients',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`POST /clients`

> Body parameter

```json
{
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "nearLocation": true,
  "partnerId": 0,
  "promoFriends": true,
  "clientPhone": "string",
  "birthDate": "2019-08-24",
  "contractPeriod": 1,
  "groupVisits": true
}
```

<h3 id="create-client-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[ClientCreateRequestDTO](#schemaclientcreaterequestdto)|true|none|

> Example responses

> 201 Response

```json
{
  "id": 0,
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "clientPhone": "string",
  "nearLocation": true,
  "age": 0
}
```

<h3 id="create-client-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Client created successfully|[ClientResponseDTO](#schemaclientresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Validation error|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Missing or invalid JWT token|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

## Get client by ID

<a id="opIdgetClientById"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/clients/{id} \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript

const headers = {
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/clients/{id}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`GET /clients/{id}`

<h3 id="get-client-by-id-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|id|path|integer(int64)|true|none|

> Example responses

> 200 Response

```json
{
  "id": 0,
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "clientPhone": "string",
  "nearLocation": true,
  "age": 0
}
```

<h3 id="get-client-by-id-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Client found|[ClientResponseDTO](#schemaclientresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid ID format provided|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Missing or invalid JWT token|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Client not found with the given ID|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

## Delete client

<a id="opIddeleteClient"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:8080/clients/{id} \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript

const headers = {
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/clients/{id}',
{
  method: 'DELETE',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`DELETE /clients/{id}`

<h3 id="delete-client-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|id|path|integer(int64)|true|none|

> Example responses

> 401 Response

```json
{
  "timestamp": "2019-08-24T14:15:22Z",
  "status": 0,
  "desc": "string",
  "message": "string",
  "fields": [
    {
      "field": "string",
      "message": "string"
    }
  ]
}
```

<h3 id="delete-client-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|204|[No Content](https://tools.ietf.org/html/rfc7231#section-6.3.5)|Client deactivated successfully|None|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Missing or invalid JWT token|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Client not found|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

## Get churn prediction for client

<a id="opIdgetClientPrediction"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/clients/{id}/prediction \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript

const headers = {
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/clients/{id}/prediction',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`GET /clients/{id}/prediction`

<h3 id="get-churn-prediction-for-client-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|id|path|integer(int64)|true|none|

> Example responses

> 200 Response

```json
{
  "id": 0,
  "clientName": "string",
  "clientPhone": "string",
  "churn": "string",
  "probability": 0.1,
  "timestamp": "2019-08-24T14:15:22Z"
}
```

<h3 id="get-churn-prediction-for-client-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Prediction generated successfully|[ClientFullResponseDTO](#schemaclientfullresponsedto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid client or prediction error|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Missing or invalid JWT token|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|Client not found|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

<h1 id="openapi-definition-authentication">Authentication</h1>

Endpoints for authentication and JWT token generation

## User login

<a id="opIdlogin"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/login \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

```javascript
const inputBody = '{
  "email": "string",
  "password": "string"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('http://localhost:8080/login',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

`POST /login`

Authenticates a user using email and password and returns a JWT token

> Body parameter

```json
{
  "email": "string",
  "password": "string"
}
```

<h3 id="user-login-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[LoginRequestDTO](#schemaloginrequestdto)|true|none|

> Example responses

> 200 Response

```json
{
  "token": "string"
}
```

<h3 id="user-login-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Authentication successful|[TokenDTO](#schematokendto)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Validation error|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Invalid credentials|[ErrorStatusResponseDTO](#schemaerrorstatusresponsedto)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-key
</aside>

# Schemas

<h2 id="tocS_ClientUpdateRequestDTO">ClientUpdateRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaclientupdaterequestdto"></a>
<a id="schema_ClientUpdateRequestDTO"></a>
<a id="tocSclientupdaterequestdto"></a>
<a id="tocsclientupdaterequestdto"></a>

```json
{
  "id": 1,
  "clientName": "string",
  "clientPhone": "string",
  "nearLocation": true,
  "birthDate": "2019-08-24"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|true|none|none|
|clientName|string|false|none|none|
|clientPhone|string|false|none|none|
|nearLocation|boolean|false|none|none|
|birthDate|string(date)|false|none|none|

<h2 id="tocS_ErrorStatusResponseDTO">ErrorStatusResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaerrorstatusresponsedto"></a>
<a id="schema_ErrorStatusResponseDTO"></a>
<a id="tocSerrorstatusresponsedto"></a>
<a id="tocserrorstatusresponsedto"></a>

```json
{
  "timestamp": "2019-08-24T14:15:22Z",
  "status": 0,
  "desc": "string",
  "message": "string",
  "fields": [
    {
      "field": "string",
      "message": "string"
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|timestamp|string(date-time)|false|none|none|
|status|integer(int32)|false|none|none|
|desc|string|false|none|none|
|message|string|false|none|none|
|fields|[[ErrorStatusResponseFieldDTO](#schemaerrorstatusresponsefielddto)]|false|none|none|

<h2 id="tocS_ErrorStatusResponseFieldDTO">ErrorStatusResponseFieldDTO</h2>
<!-- backwards compatibility -->
<a id="schemaerrorstatusresponsefielddto"></a>
<a id="schema_ErrorStatusResponseFieldDTO"></a>
<a id="tocSerrorstatusresponsefielddto"></a>
<a id="tocserrorstatusresponsefielddto"></a>

```json
{
  "field": "string",
  "message": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|field|string|false|none|none|
|message|string|false|none|none|

<h2 id="tocS_ClientResponseDTO">ClientResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaclientresponsedto"></a>
<a id="schema_ClientResponseDTO"></a>
<a id="tocSclientresponsedto"></a>
<a id="tocsclientresponsedto"></a>

```json
{
  "id": 0,
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "clientPhone": "string",
  "nearLocation": true,
  "age": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|clientName|string|false|none|none|
|active|boolean|false|none|none|
|gender|string|false|none|none|
|clientPhone|string|false|none|none|
|nearLocation|boolean|false|none|none|
|age|integer(int32)|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|gender|MALE|
|gender|FEMALE|

<h2 id="tocS_LoginRequestDTO">LoginRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaloginrequestdto"></a>
<a id="schema_LoginRequestDTO"></a>
<a id="tocSloginrequestdto"></a>
<a id="tocsloginrequestdto"></a>

```json
{
  "email": "string",
  "password": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|true|none|none|
|password|string|true|none|none|

<h2 id="tocS_TokenDTO">TokenDTO</h2>
<!-- backwards compatibility -->
<a id="schematokendto"></a>
<a id="schema_TokenDTO"></a>
<a id="tocStokendto"></a>
<a id="tocstokendto"></a>

```json
{
  "token": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|token|string|false|none|none|

<h2 id="tocS_ClientCreateRequestDTO">ClientCreateRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaclientcreaterequestdto"></a>
<a id="schema_ClientCreateRequestDTO"></a>
<a id="tocSclientcreaterequestdto"></a>
<a id="tocsclientcreaterequestdto"></a>

```json
{
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "nearLocation": true,
  "partnerId": 0,
  "promoFriends": true,
  "clientPhone": "string",
  "birthDate": "2019-08-24",
  "contractPeriod": 1,
  "groupVisits": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|clientName|string|true|none|none|
|active|boolean|true|none|none|
|gender|string|true|none|none|
|nearLocation|boolean|true|none|none|
|partnerId|integer(int64)|false|none|none|
|promoFriends|boolean|true|none|none|
|clientPhone|string|true|none|none|
|birthDate|string(date)|true|none|none|
|contractPeriod|integer(int32)|true|none|none|
|groupVisits|boolean|true|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|gender|MALE|
|gender|FEMALE|

<h2 id="tocS_Pageable">Pageable</h2>
<!-- backwards compatibility -->
<a id="schemapageable"></a>
<a id="schema_Pageable"></a>
<a id="tocSpageable"></a>
<a id="tocspageable"></a>

```json
{
  "page": 0,
  "size": 1,
  "sort": [
    "string"
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|page|integer(int32)|false|none|none|
|size|integer(int32)|false|none|none|
|sort|[string]|false|none|none|

<h2 id="tocS_ClientListResponseDTO">ClientListResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaclientlistresponsedto"></a>
<a id="schema_ClientListResponseDTO"></a>
<a id="tocSclientlistresponsedto"></a>
<a id="tocsclientlistresponsedto"></a>

```json
{
  "id": 0,
  "clientName": "string",
  "active": true,
  "gender": "MALE",
  "clientPhone": "string",
  "nearLocation": true,
  "age": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|clientName|string|false|none|none|
|active|boolean|false|none|none|
|gender|string|false|none|none|
|clientPhone|string|false|none|none|
|nearLocation|boolean|false|none|none|
|age|integer(int32)|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|gender|MALE|
|gender|FEMALE|

<h2 id="tocS_PageClientListResponseDTO">PageClientListResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemapageclientlistresponsedto"></a>
<a id="schema_PageClientListResponseDTO"></a>
<a id="tocSpageclientlistresponsedto"></a>
<a id="tocspageclientlistresponsedto"></a>

```json
{
  "totalElements": 0,
  "totalPages": 0,
  "pageable": {
    "paged": true,
    "pageNumber": 0,
    "pageSize": 0,
    "offset": 0,
    "sort": {
      "sorted": true,
      "empty": true,
      "unsorted": true
    },
    "unpaged": true
  },
  "size": 0,
  "content": [
    {
      "id": 0,
      "clientName": "string",
      "active": true,
      "gender": "MALE",
      "clientPhone": "string",
      "nearLocation": true,
      "age": 0
    }
  ],
  "number": 0,
  "sort": {
    "sorted": true,
    "empty": true,
    "unsorted": true
  },
  "first": true,
  "last": true,
  "numberOfElements": 0,
  "empty": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|totalElements|integer(int64)|false|none|none|
|totalPages|integer(int32)|false|none|none|
|pageable|[PageableObject](#schemapageableobject)|false|none|none|
|size|integer(int32)|false|none|none|
|content|[[ClientListResponseDTO](#schemaclientlistresponsedto)]|false|none|none|
|number|integer(int32)|false|none|none|
|sort|[SortObject](#schemasortobject)|false|none|none|
|first|boolean|false|none|none|
|last|boolean|false|none|none|
|numberOfElements|integer(int32)|false|none|none|
|empty|boolean|false|none|none|

<h2 id="tocS_PageableObject">PageableObject</h2>
<!-- backwards compatibility -->
<a id="schemapageableobject"></a>
<a id="schema_PageableObject"></a>
<a id="tocSpageableobject"></a>
<a id="tocspageableobject"></a>

```json
{
  "paged": true,
  "pageNumber": 0,
  "pageSize": 0,
  "offset": 0,
  "sort": {
    "sorted": true,
    "empty": true,
    "unsorted": true
  },
  "unpaged": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|paged|boolean|false|none|none|
|pageNumber|integer(int32)|false|none|none|
|pageSize|integer(int32)|false|none|none|
|offset|integer(int64)|false|none|none|
|sort|[SortObject](#schemasortobject)|false|none|none|
|unpaged|boolean|false|none|none|

<h2 id="tocS_SortObject">SortObject</h2>
<!-- backwards compatibility -->
<a id="schemasortobject"></a>
<a id="schema_SortObject"></a>
<a id="tocSsortobject"></a>
<a id="tocssortobject"></a>

```json
{
  "sorted": true,
  "empty": true,
  "unsorted": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|sorted|boolean|false|none|none|
|empty|boolean|false|none|none|
|unsorted|boolean|false|none|none|

<h2 id="tocS_ClientFullResponseDTO">ClientFullResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaclientfullresponsedto"></a>
<a id="schema_ClientFullResponseDTO"></a>
<a id="tocSclientfullresponsedto"></a>
<a id="tocsclientfullresponsedto"></a>

```json
{
  "id": 0,
  "clientName": "string",
  "clientPhone": "string",
  "churn": "string",
  "probability": 0.1,
  "timestamp": "2019-08-24T14:15:22Z"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|none|
|clientName|string|false|none|none|
|clientPhone|string|false|none|none|
|churn|string(byte)|false|none|none|
|probability|number(double)|false|none|none|
|timestamp|string(date-time)|false|none|none|

