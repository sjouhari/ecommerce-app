import 'package:flutter/material.dart';
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

    _token = token;

    notifyListeners();
  }

  Future<void> logout() async {
    _token = null;

    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');

    notifyListeners();
  }

  Future<void> tryAutoLogin() async {
    final prefs = await SharedPreferences.getInstance();
    if (!prefs.containsKey('token')) return;
    _token = prefs.getString('token');
    notifyListeners();
  }
}
