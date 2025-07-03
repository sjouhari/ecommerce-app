import 'package:mobile_app/models/inventory.dart';
import 'package:mobile_app/models/media.dart';

class Product {
  final int id;
  final String name;
  final String description;
  final int categoryId;
  final String categoryName;
  final int subCategoryId;
  final String subCategoryName;
  final List<Media> medias;
  final List<Inventory> stock;

  Product({
    required this.id,
    required this.name,
    required this.description,
    required this.categoryId,
    required this.categoryName,
    required this.subCategoryId,
    required this.subCategoryName,
    required this.medias,
    required this.stock,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      categoryId: json['categoryId'],
      categoryName: json['categoryName'],
      subCategoryId: json['subCategoryId'],
      subCategoryName: json['subCategoryName'],
      medias:
          (json['medias'] as List<dynamic>)
              .map((mediaJson) => Media.fromJson(mediaJson))
              .toList(),
      stock:
          (json['stock'] as List<dynamic>)
              .map((inventoryJson) => Inventory.fromJson(inventoryJson))
              .toList(),
    );
  }
}
