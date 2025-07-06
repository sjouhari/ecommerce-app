import 'dart:convert';

import 'package:mobile_app/models/address.dart';
import 'package:mobile_app/models/user.dart';

import '../api/api_client.dart';

class AddressService {
  Future<List<Address>> fetchUserAddresses() async {
    User? user = await ApiClient.getCurrentUser();
    try {
      final response = await ApiClient.get(
        endpoint: 'addresses/user/${user!.id}',
        auth: true,
      );

      if (response.statusCode == 200) {
        final List<dynamic> jsonList = json.decode(response.body);
        return jsonList.map((json) => Address.fromJson(json)).toList();
      } else {
        throw Exception(
          'Failed to load user delivery addresses: ${response.statusCode}',
        );
      }
    } catch (e) {
      throw Exception('Error fetching addresses: $e');
    }
  }

  Future<Address> addAddress(Address address) async {
    try {
      final response = await ApiClient.post(
        endpoint: 'addresses',
        body: address.toJson(),
        auth: true,
      );

      if (response.statusCode == 200) {
        return Address.fromJson(json.decode(response.body));
      } else {
        throw Exception('Error adding address: ${response.body}');
      }
    } catch (e) {
      throw Exception('Error adding address: $e');
    }
  }
}
