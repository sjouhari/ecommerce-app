class Address {
  final int id;
  final int userId;
  final String firstName;
  final String lastName;
  final String phone;
  final String deliveryAddress;
  final String country;
  final String city;
  final String postalCode;

  Address({
    required this.id,
    required this.userId,
    required this.firstName,
    required this.lastName,
    required this.phone,
    required this.deliveryAddress,
    required this.country,
    required this.city,
    required this.postalCode,
  });

  factory Address.fromJson(Map<String, dynamic> json) {
    return Address(
      id: json['id'],
      userId: json['userId'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      phone: json['phone'],
      deliveryAddress: json['deliveryAddress'],
      country: json['country'],
      city: json['city'],
      postalCode: json['postalCode'],
    );
  }

  Map<String, dynamic> toJson() => {
    'userId': userId,
    'firstName': firstName,
    'lastName': lastName,
    'phone': phone,
    'deliveryAddress': deliveryAddress,
    'country': country,
    'city': city,
    'postalCode': postalCode,
  };
}
