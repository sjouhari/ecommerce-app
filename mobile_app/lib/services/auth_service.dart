import 'dart:convert';

import '../api/api_client.dart';
import '../models/auth_response.dart';

class AuthService {
  Future<String?> login(String email, String password) async {
    final response = await ApiClient.post(
      endpoint: 'auth/login',
      body: {'email': email, 'password': password},
    );

    if (response.statusCode == 200) {
      final auth = AuthResponse.fromJson(jsonDecode(response.body));
      await ApiClient.saveToken(auth.token);
      return null;
    } else if (response.statusCode == 500) {
      return jsonDecode(response.body)['message'];
    } else {
      return 'Echec de connexion!';
    }
  }

  Future<String?> register(
    String firstName,
    String lastName,
    String email,
    String password,
  ) async {
    final response = await ApiClient.post(
      endpoint: 'auth/register',
      body: {
        'firstName': firstName,
        'lastName': lastName,
        'email': email,
        'password': password,
      },
    );

    if (response.statusCode == 201) {
      return null;
    } else if (response.statusCode == 500) {
      return jsonDecode(response.body)['message'];
    } else {
      return 'Echec de l\'inscription!';
    }
  }
}
