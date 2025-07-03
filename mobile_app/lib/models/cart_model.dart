import 'package:flutter/foundation.dart';

class CartItem {
  final String id;
  final String name;
  final double price;
  final String image;
  int quantity;
  final String? selectedColor;
  final String? selectedSize;
  final String? selectedSpec; // Pour les spécifications comme la taille d'écran

  CartItem({
    required this.id,
    required this.name,
    required this.price,
    required this.image,
    this.quantity = 1,
    this.selectedColor,
    this.selectedSize,
    this.selectedSpec,
  });

  // Créer un ID unique basé sur le produit et ses options
  String get uniqueId {
    List<String> idParts = [id];
    if (selectedColor != null) idParts.add('color:$selectedColor');
    if (selectedSize != null) idParts.add('size:$selectedSize');
    if (selectedSpec != null) idParts.add('spec:$selectedSpec');
    return idParts.join('_');
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'price': price,
      'image': image,
      'quantity': quantity,
      'selectedColor': selectedColor,
      'selectedSize': selectedSize,
      'selectedSpec': selectedSpec,
    };
  }
}

class CartModel extends ChangeNotifier {
  final List<CartItem> _items = [];

  List<CartItem> get items => _items;

  int get itemCount => _items.fold(0, (sum, item) => sum + item.quantity);

  double get totalPrice =>
      _items.fold(0.0, (sum, item) => sum + (item.price * item.quantity));

  void addItem(
    String id,
    String name,
    double price,
    String image, {
    String? selectedColor,
    String? selectedSize,
    String? selectedSpec,
  }) {
    final newItem = CartItem(
      id: id,
      name: name,
      price: price,
      image: image,
      selectedColor: selectedColor,
      selectedSize: selectedSize,
      selectedSpec: selectedSpec,
    );

    final existingIndex = _items.indexWhere(
      (item) => item.uniqueId == newItem.uniqueId,
    );

    if (existingIndex >= 0) {
      _items[existingIndex].quantity++;
    } else {
      _items.add(newItem);
    }
    notifyListeners();
  }

  void removeItem(String uniqueId) {
    _items.removeWhere((item) => item.uniqueId == uniqueId);
    notifyListeners();
  }

  void updateQuantity(String uniqueId, int quantity) {
    final index = _items.indexWhere((item) => item.uniqueId == uniqueId);
    if (index >= 0) {
      if (quantity <= 0) {
        _items.removeAt(index);
      } else {
        _items[index].quantity = quantity;
      }
      notifyListeners();
    }
  }

  void clearCart() {
    _items.clear();
    notifyListeners();
  }
}
