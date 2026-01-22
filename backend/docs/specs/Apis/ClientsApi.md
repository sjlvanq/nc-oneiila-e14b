# ClientsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createClient**](ClientsApi.md#createClient) | **POST** /clients | Create client |
| [**deleteClient**](ClientsApi.md#deleteClient) | **DELETE** /clients/{id} | Delete client |
| [**getClientById**](ClientsApi.md#getClientById) | **GET** /clients/{id} | Get client by ID |
| [**getClientList**](ClientsApi.md#getClientList) | **GET** /clients | List active clients |
| [**getClientPredictionByDni**](ClientsApi.md#getClientPredictionByDni) | **GET** /clients/prediction/{dni} | Get churn prediction for client by DNI |
| [**getClientStatistics**](ClientsApi.md#getClientStatistics) | **GET** /clients/clients/statistics/{id} | Get statistics for client by ID |
| [**updateClient**](ClientsApi.md#updateClient) | **PUT** /clients | Update client |


<a name="createClient"></a>
# **createClient**
> ClientResponseDTO createClient(ClientCreateRequestDTO)

Create client

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ClientCreateRequestDTO** | [**ClientCreateRequestDTO**](../Models/ClientCreateRequestDTO.md)|  | |

### Return type

[**ClientResponseDTO**](../Models/ClientResponseDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="deleteClient"></a>
# **deleteClient**
> deleteClient(id)

Delete client

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

null (empty response body)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getClientById"></a>
# **getClientById**
> ClientResponseDTO getClientById(id)

Get client by ID

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**ClientResponseDTO**](../Models/ClientResponseDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getClientList"></a>
# **getClientList**
> PageClientListResponseDTO getClientList(pageable)

List active clients

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **pageable** | [**Pageable**](../Models/.md)|  | [default to null] |

### Return type

[**PageClientListResponseDTO**](../Models/PageClientListResponseDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getClientPredictionByDni"></a>
# **getClientPredictionByDni**
> ClientFullResponseDTO getClientPredictionByDni(dni)

Get churn prediction for client by DNI

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **dni** | **String**|  | [default to null] |

### Return type

[**ClientFullResponseDTO**](../Models/ClientFullResponseDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="getClientStatistics"></a>
# **getClientStatistics**
> ClientStatisticsDTO getClientStatistics(id)

Get statistics for client by ID

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Long**|  | [default to null] |

### Return type

[**ClientStatisticsDTO**](../Models/ClientStatisticsDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="updateClient"></a>
# **updateClient**
> ClientResponseDTO updateClient(ClientUpdateRequestDTO)

Update client

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **ClientUpdateRequestDTO** | [**ClientUpdateRequestDTO**](../Models/ClientUpdateRequestDTO.md)|  | |

### Return type

[**ClientResponseDTO**](../Models/ClientResponseDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

