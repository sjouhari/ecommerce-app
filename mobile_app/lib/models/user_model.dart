class UserModel {
  final String id;
  final String firstName;
  final String lastName;
  final String email;

  UserModel(this.id, this.firstName, this.lastName, {required this.email});

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      json['id'],
      json['firstName'],
      json['lastName'],
      email: json['email'],
    );
  }
}
