import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile_app/models/user.dart';

class ApiClient {
  static const baseUrl = "http://10.0.2.2:8080/api"; // localhost Android
  // static const baseUrl = "http://192.168.18.140:8080/api"; // Physique device
  static final _storage = FlutterSecureStorage();

  static Future<void> saveToken(String token) async {
    await _storage.write(key: "token", value: token);
  }

  static Future<String?> getToken() async {
    return await _storage.read(key: "token");
  }

  static Future<void> clearToken() async {
    await _storage.delete(key: "token");
  }

  static Future<void> saveCurrentUser() async {
    final response = await get(endpoint: 'auth/current-user', auth: true);
    if (response.statusCode == 200) {
      final user = jsonDecode(response.body);
      return await _storage.write(key: "user", value: jsonEncode(user));
    } else {
      throw Exception('Failed to save current user: ${response.statusCode}');
    }
  }

  static Future<User?> getCurrentUser() async {
    final userJson = await _storage.read(key: "user");
    if (userJson != null) {
      return User.fromJson(jsonDecode(userJson));
    }
    return null;
  }

  static Future<http.Response> get({
    required String endpoint,
    bool auth = false,
  }) async {
    final headers = {'Content-Type': 'application/json'};
    if (auth) {
      final token = await getToken();
      if (token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
    }

    print('$baseUrl/$endpoint');
    final url = Uri.parse('$baseUrl/$endpoint');
    return await http.get(url, headers: headers);
  }

  static Future<http.Response> post({
    required String endpoint,
    required Map<String, dynamic> body,
    bool auth = false,
  }) async {
    final headers = {'Content-Type': 'application/json'};
    if (auth) {
      final token = await getToken();
      if (token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
    }

    final url = Uri.parse('$baseUrl/$endpoint');
    return await http.post(url, headers: headers, body: jsonEncode(body));
  }

  static Future<http.Response> put({
    required String endpoint,
    required Map<String, dynamic> body,
    bool auth = false,
  }) async {
    final headers = {'Content-Type': 'application/json'};
    if (auth) {
      final token = await getToken();
      if (token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
    }

    final url = Uri.parse('$baseUrl/$endpoint');
    return await http.put(url, headers: headers, body: jsonEncode(body));
  }

  static Future<http.Response> delete({
    required String endpoint,
    bool auth = false,
  }) async {
    final headers = {'Content-Type': 'application/json'};
    if (auth) {
      final token = await getToken();
      if (token != null) {
        headers['Authorization'] = 'Bearer $token';
      }
    }

    final url = Uri.parse('$baseUrl/$endpoint');
    return await http.delete(url, headers: headers);
  }
}
