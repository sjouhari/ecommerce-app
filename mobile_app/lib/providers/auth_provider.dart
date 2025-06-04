import 'package:flutter/material.dart';
import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:mobile_app/models/user_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AuthProvider with ChangeNotifier {
  String? _token;
  UserModel? _currentUser;

  bool get isAuthenticated => _token != null;
  String? get token => _token;
  UserModel? get currentUser => _currentUser;

  Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('token', token);

    verifyToken(token);
  }

  Future<void> logout() async {
    _token = null;

    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');

    notifyListeners();
  }

  Future<void> tryAutoLogin() async {
    final prefs = await SharedPreferences.getInstance();
    print("AAAA");
    if (!prefs.containsKey('token')) return;
    print("BBBB");
    verifyToken(prefs.getString('token')!);
  }

  Future<bool> verifyToken(String token) async {
    // Vérifie si expiré
    bool isExpired = JwtDecoder.isExpired(token);

    if (isExpired) {
      final prefs = await SharedPreferences.getInstance();
      prefs.remove('token');
      _token = null;
      notifyListeners();
      return false;
    }

    // Décoder les claims
    Map<String, dynamic> decodedToken = JwtDecoder.decode(token);
    _currentUser = UserModel.fromJson(decodedToken['user']);
    _token = token;
    notifyListeners();
    return true;
  }
}
