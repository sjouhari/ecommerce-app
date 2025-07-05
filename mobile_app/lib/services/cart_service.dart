import 'dart:convert';

import 'package:mobile_app/models/cart_model.dart';
import 'package:mobile_app/models/order_item.dart';
import 'package:mobile_app/models/user.dart';

import '../api/api_client.dart';

class CartService {
  Future<CartModel> fetchUserCart() async {
    try {
      User? user = await ApiClient.getCurrentUser();

      final response = await ApiClient.get(
        endpoint: 'shopping-cart/${user!.id}',
        auth: true,
      );
      if (response.statusCode == 200) {
        return CartModel.fromJson(json.decode(response.body));
      } else if (response.statusCode == 404) {
        return CartModel(id: 0, userId: 0, orderItems: []);
      } else {
        throw Exception('Failed to load user cart: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error fetching user cart: $e');
    }
  }

  Future<bool> clearUserCart() async {
    try {
      User? user = await ApiClient.getCurrentUser();
      final response = await ApiClient.delete(
        endpoint: 'shopping-cart/user/${user!.id}',
        auth: true,
      );
      if (response.statusCode == 204) {
        return true;
      } else {
        throw Exception(
          "Failed to clear cart items ${response.statusCode} ${response.body}",
        );
      }
    } catch (e) {
      throw Exception('Error clearing user cart: $e');
    }
  }

  Future<bool> addOrderItemToCart(OrderItem orderItem) async {
    try {
      User? user = await ApiClient.getCurrentUser();
      final response = await ApiClient.post(
        endpoint: "shopping-cart/${user!.id}",
        body: orderItem.toJson(),
        auth: true,
      );

      if (response.statusCode == 200) {
        return true;
      } else {
        throw Exception('Error adding product to cart: ${response.body}');
      }
    } catch (e) {
      throw Exception('Error adding product to cart: $e');
    }
  }

  Future<bool> removeOrderItemFromCart(int orderItemId) async {
    try {
      final response = await ApiClient.delete(
        endpoint: 'shopping-cart/$orderItemId',
        auth: true,
      );
      if (response.statusCode == 204) {
        return true;
      } else {
        throw Exception("Failed to delete item");
      }
    } catch (e) {
      throw Exception('Error removing product from cart: $e');
    }
  }

  Future<bool> updateOrderItemQuantityInCart(
    int orderItemId,
    int quantity,
  ) async {
    try {
      final response = await ApiClient.put(
        endpoint: 'shopping-cart/quantity/$orderItemId',
        body: {'quantity': quantity},
        auth: true,
      );

      if (response.statusCode == 200) {
        return true;
      } else {
        throw Exception("Failed to update quantity");
      }
    } catch (e) {
      throw Exception('Error updating product quantity in cart: $e');
    }
  }
}
