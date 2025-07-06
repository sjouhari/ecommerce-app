import 'package:mobile_app/models/order_request.dart';

import '../api/api_client.dart';

class OrderService {
  // Future<List<Address>> fetchUserAddresses() async {
  //   User? user = await ApiClient.getCurrentUser();
  //   try {
  //     final response = await ApiClient.get(
  //       endpoint: 'orders/${user!.id}',
  //       auth: true,
  //     );

  //     if (response.statusCode == 200) {
  //       final List<dynamic> jsonList = json.decode(response.body);
  //       return jsonList.map((json) => Address.fromJson(json)).toList();
  //     } else {
  //       throw Exception(
  //         'Failed to load user delivery addresses: ${response.statusCode}',
  //       );
  //     }
  //   } catch (e) {
  //     throw Exception('Error fetching addresses: $e');
  //   }
  // }

  Future<bool> placeOrder(OrderRequest order) async {
    try {
      final response = await ApiClient.post(
        endpoint: 'orders',
        body: order.toJson(),
        auth: true,
      );

      if (response.statusCode == 201) {
        return true;
      } else {
        throw Exception('Error placing order: ${response.body}');
      }
    } catch (e) {
      throw Exception('Error placing order: $e');
    }
  }
}
