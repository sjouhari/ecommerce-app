import 'dart:convert';

import 'package:mobile_app/models/product.dart';

import '../api/api_client.dart';

class ProductService {
  Future<List<Product>> fetchProducts() async {
    try {
      final response = await ApiClient.get(endpoint: 'products');
      if (response.statusCode == 200) {
        final List<dynamic> jsonList = json.decode(response.body);
        return jsonList.map((json) => Product.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load products: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error fetching products: $e');
    }
  }
}
