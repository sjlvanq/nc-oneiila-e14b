# AuthenticationApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**login**](AuthenticationApi.md#login) | **POST** /login | User login |


<a name="login"></a>
# **login**
> TokenDTO login(LoginRequestDTO)

User login

    Authenticates a user using email and password and returns a JWT token

### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **LoginRequestDTO** | [**LoginRequestDTO**](../Models/LoginRequestDTO.md)|  | |

### Return type

[**TokenDTO**](../Models/TokenDTO.md)

### Authorization

[bearer-key](../README.md#bearer-key)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

