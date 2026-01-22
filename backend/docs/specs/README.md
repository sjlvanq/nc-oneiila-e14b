# Documentation for OpenAPI definition

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

| Class | Method | HTTP request | Description |
|------------ | ------------- | ------------- | -------------|
| *AuthenticationApi* | [**login**](Apis/AuthenticationApi.md#login) | **POST** /login | User login |
| *ClientsApi* | [**createClient**](Apis/ClientsApi.md#createClient) | **POST** /clients | Create client |
*ClientsApi* | [**deleteClient**](Apis/ClientsApi.md#deleteClient) | **DELETE** /clients/{id} | Delete client |
*ClientsApi* | [**getClientById**](Apis/ClientsApi.md#getClientById) | **GET** /clients/{id} | Get client by ID |
*ClientsApi* | [**getClientList**](Apis/ClientsApi.md#getClientList) | **GET** /clients | List active clients |
*ClientsApi* | [**getClientPredictionByDni**](Apis/ClientsApi.md#getClientPredictionByDni) | **GET** /clients/prediction/{dni} | Get churn prediction for client by DNI |
*ClientsApi* | [**getClientStatistics**](Apis/ClientsApi.md#getClientStatistics) | **GET** /clients/clients/statistics/{id} | Get statistics for client by ID |
*ClientsApi* | [**updateClient**](Apis/ClientsApi.md#updateClient) | **PUT** /clients | Update client |
| *GlobalStatsControllerApi* | [**getGlobalStats1**](Apis/GlobalStatsControllerApi.md#getGlobalStats1) | **GET** /api/stats/global |  |
| *StatisticsApi* | [**getGlobalStats**](Apis/StatisticsApi.md#getGlobalStats) | **GET** /api/stats | Get statistics |


<a name="documentation-for-models"></a>
## Documentation for Models

 - [AdditionalChargesDTO](./Models/AdditionalChargesDTO.md)
 - [CategoryChargeDTO](./Models/CategoryChargeDTO.md)
 - [ClientCreateRequestDTO](./Models/ClientCreateRequestDTO.md)
 - [ClientFullResponseDTO](./Models/ClientFullResponseDTO.md)
 - [ClientListResponseDTO](./Models/ClientListResponseDTO.md)
 - [ClientResponseDTO](./Models/ClientResponseDTO.md)
 - [ClientStatisticsDTO](./Models/ClientStatisticsDTO.md)
 - [ClientUpdateRequestDTO](./Models/ClientUpdateRequestDTO.md)
 - [ErrorStatusResponseDTO](./Models/ErrorStatusResponseDTO.md)
 - [ErrorStatusResponseFieldDTO](./Models/ErrorStatusResponseFieldDTO.md)
 - [GlobalStatisticsDTO](./Models/GlobalStatisticsDTO.md)
 - [LoginRequestDTO](./Models/LoginRequestDTO.md)
 - [PageClientListResponseDTO](./Models/PageClientListResponseDTO.md)
 - [Pageable](./Models/Pageable.md)
 - [PageableObject](./Models/PageableObject.md)
 - [SortObject](./Models/SortObject.md)
 - [TokenDTO](./Models/TokenDTO.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="bearer-key"></a>
### bearer-key

- **Type**: HTTP Bearer Token authentication (JWT)

