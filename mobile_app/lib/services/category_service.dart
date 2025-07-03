import 'dart:convert';

import 'package:mobile_app/models/category.dart';

import '../api/api_client.dart';

class CategoryService {
  Future<List<Category>> fetchCategories() async {
    try {
      final response = await ApiClient.get(endpoint: 'categories');

      if (response.statusCode == 200) {
        final List<dynamic> jsonList = json.decode(response.body);
        return jsonList.map((json) => Category.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load categories: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error fetching categories: $e');
    }
  }
}
