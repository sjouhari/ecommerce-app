import 'package:flutter/material.dart';
import 'package:mobile_app/api/api_client.dart';
import 'package:mobile_app/models/order_item.dart';
import 'package:mobile_app/models/product.dart';
import 'package:mobile_app/services/cart_service.dart';
import '../../models/cart_model.dart';
import '../../models/product_options.dart';

class ProductDetailScreen extends StatefulWidget {
  final Product product;

  const ProductDetailScreen({super.key, required this.product});

  @override
  State<ProductDetailScreen> createState() => _ProductDetailScreenState();
}

class _ProductDetailScreenState extends State<ProductDetailScreen> {
  int _quantity = 1;
  String? _selectedColor;
  String? _selectedSize;
  double? _productPrice;
  List<String> colors = [];
  List<String> sizes = [];
  CartModel? cart;

  final _cartService = CartService();

  @override
  void initState() {
    super.initState();

    sizes = widget.product.stock.map((s) => s.size).toSet().toList();
    if (sizes.isNotEmpty) {
      _selectedSize = sizes.first;
    }

    colors =
        widget.product.stock
            .where((s) => s.size == _selectedSize)
            .map((s) => s.color)
            .toSet()
            .toList();
    if (colors.isNotEmpty) {
      _selectedColor = colors.first;
    }

    _productPrice =
        widget.product.stock
            .firstWhere(
              (s) => s.size == _selectedSize && s.color == _selectedColor,
            )
            .price;

    _fetchUserCart();
  }

  void _fetchUserCart() {
    _cartService.fetchUserCart().then(
      (cart) => setState(() => this.cart = cart),
    );
  }

  bool get _canAddToCart {
    final category = widget.product.categoryName;
    final colors = ProductOptions.getColorsForCategory(category);
    final sizes = ProductOptions.getSizesForCategory(category);

    // Vérifier que toutes les options requises sont sélectionnées
    if (colors != null && _selectedColor == null) return false;
    if (sizes != null && _selectedSize == null) return false;

    return true;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.product.name),
        actions: [
          Stack(
            children: [
              IconButton(
                icon: const Icon(Icons.shopping_cart_outlined),
                onPressed: () async {
                  await Navigator.pushNamed(context, '/cart');
                  _fetchUserCart();
                },
              ),
              if (cart != null && cart!.orderItems.isNotEmpty)
                Positioned(
                  right: 8,
                  top: 8,
                  child: Container(
                    padding: const EdgeInsets.all(2),
                    decoration: BoxDecoration(
                      color: Colors.red,
                      borderRadius: BorderRadius.circular(10),
                    ),
                    constraints: const BoxConstraints(
                      minWidth: 16,
                      minHeight: 16,
                    ),
                    child: Text(
                      '${cart!.orderItems.length}',
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 12,
                        fontWeight: FontWeight.bold,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
            ],
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Image du produit
            Container(
              width: double.infinity,
              height: 300,
              color: Colors.grey[100],
              child: Image.network(
                "${ApiClient.baseUrl}/products/images/${widget.product.medias[0].url}",
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) {
                  return Container(
                    color: Colors.grey[200],
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.image_not_supported,
                          size: 80,
                          color: Colors.grey[400],
                        ),
                        const SizedBox(height: 16),
                        Text(
                          widget.product.name,
                          style: TextStyle(
                            color: Colors.grey[600],
                            fontSize: 16,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  );
                },
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Nom et catégorie
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          widget.product.name,
                          style: const TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  // Description
                  const Text(
                    'Description',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    widget.product.description,
                    style: TextStyle(
                      fontSize: 16,
                      color: Colors.grey[700],
                      height: 1.5,
                    ),
                  ),

                  const SizedBox(height: 24),

                  // Catégorie & Price
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 12,
                          vertical: 6,
                        ),
                        decoration: BoxDecoration(
                          color: const Color(0xFF2196F3).withOpacity(0.1),
                          borderRadius: BorderRadius.circular(20),
                        ),
                        child: Text(
                          '${widget.product.categoryName} - ${widget.product.subCategoryName}',
                          style: const TextStyle(
                            fontSize: 18,
                            fontWeight: FontWeight.w500,
                            color: Color(0xFF2196F3),
                          ),
                        ),
                      ),

                      // Prix
                      Text(
                        '${_productPrice?.toStringAsFixed(2)}€',
                        style: const TextStyle(
                          fontSize: 26,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF2196F3),
                        ),
                      ),
                    ],
                  ),

                  const SizedBox(height: 24),

                  // Options de personnalisation
                  ..._buildProductOptions(),

                  // Sélecteur de quantité
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text(
                            'Quantité :',
                            style: TextStyle(
                              fontSize: 18,
                              fontWeight: FontWeight.bold,
                            ),
                          ),

                          Container(
                            decoration: BoxDecoration(
                              border: Border.all(color: Colors.grey[300]!),
                              borderRadius: BorderRadius.circular(15),
                            ),
                            child: Row(
                              children: [
                                IconButton(
                                  onPressed:
                                      _quantity > 1
                                          ? () {
                                            setState(() {
                                              _quantity--;
                                            });
                                          }
                                          : null,
                                  icon: const Icon(Icons.remove),
                                  iconSize: 20,
                                ),
                                Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: 16,
                                  ),
                                  child: Text(
                                    '$_quantity',
                                    style: const TextStyle(
                                      fontSize: 15,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                                IconButton(
                                  style: ButtonStyle(
                                    padding:
                                        WidgetStateProperty.all<EdgeInsets>(
                                          EdgeInsets.all(0),
                                        ),
                                  ),
                                  onPressed:
                                      _quantity < 10
                                          ? () {
                                            setState(() {
                                              _quantity++;
                                            });
                                          }
                                          : null,
                                  icon: const Icon(Icons.add),
                                  iconSize: 20,
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),

                      SizedBox(height: 8),

                      Text(
                        'Prix Totale : ${(_productPrice != null ? (_productPrice! * _quantity) : 0).toStringAsFixed(2)}€',
                        style: const TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF2196F3),
                        ),
                      ),
                    ],
                  ),

                  const SizedBox(height: 32),
                ],
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: Colors.white,
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withOpacity(0.2),
              spreadRadius: 1,
              blurRadius: 5,
              offset: const Offset(0, -2),
            ),
          ],
        ),
        child: Row(
          children: [
            Expanded(
              child: ElevatedButton.icon(
                onPressed:
                    _canAddToCart
                        ? () async {
                          final response = await _cartService
                              .addOrderItemToCart(
                                OrderItem(
                                  id: 0,
                                  productId: widget.product.id,
                                  productName: widget.product.name,
                                  productImage: widget.product.medias[0].url,
                                  size: _selectedSize!,
                                  color: _selectedColor!,
                                  price: _productPrice ?? 0,
                                  quantity: _quantity,
                                  selected: true,
                                ),
                              );

                          if (response) {
                            _fetchUserCart();
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Text(
                                  '$_quantity x ${widget.product.name} ajouté(s) au panier',
                                ),
                                duration: const Duration(seconds: 2),
                                backgroundColor: Colors.green,
                                action: SnackBarAction(
                                  label: 'Voir panier',
                                  textColor: Colors.white,
                                  onPressed: () {
                                    Navigator.pushNamed(context, '/cart');
                                  },
                                ),
                              ),
                            );
                          }
                        }
                        : null,
                icon: const Icon(Icons.add_shopping_cart),
                label: const Text('Ajouter au panier'),
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  backgroundColor: _canAddToCart ? null : Colors.grey,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  List<Widget> _buildProductOptions() {
    List<Widget> options = [];

    options.addAll([
      const Text(
        'Taille :',
        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
      ),
      const SizedBox(height: 12),
      _buildSizeSelector(),
      const SizedBox(height: 12),
    ]);

    options.addAll([
      const Text(
        'Couleur :',
        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
      ),
      const SizedBox(height: 12),
      _buildColorSelector(),
    ]);

    return options;
  }

  Widget _buildColorSelector() {
    final colorHex = ProductOptions.getColorHexValues();

    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children:
          colors.map((color) {
            final isSelected = _selectedColor == color;
            final colorValue = colorHex[color] ?? 0xFF000000;

            return GestureDetector(
              onTap: () {
                setState(() {
                  _selectedColor = color;

                  _productPrice =
                      widget.product.stock
                          .firstWhere(
                            (s) =>
                                s.size == _selectedSize &&
                                s.color == _selectedColor,
                          )
                          .price;
                });
              },
              child: Container(
                width: 35,
                height: 35,
                decoration: BoxDecoration(
                  color: Color(colorValue),
                  borderRadius: BorderRadius.circular(6),
                  border: Border.all(
                    color:
                        isSelected
                            ? const Color(0xFF2196F3)
                            : Colors.grey[300]!,
                    width: isSelected ? 2.5 : 1,
                  ),
                  boxShadow:
                      isSelected
                          ? [
                            BoxShadow(
                              color: const Color(0xFF2196F3).withOpacity(0.3),
                              spreadRadius: 1,
                              blurRadius: 6,
                            ),
                          ]
                          : null,
                ),
                child:
                    color == 'Blanc'
                        ? Container(
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(6),
                            border: Border.all(
                              color: Colors.grey[300]!,
                              width: 1,
                            ),
                          ),
                        )
                        : null,
              ),
            );
          }).toList(),
    );
  }

  Widget _buildSizeSelector() {
    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children:
          sizes.map((size) {
            final isSelected = _selectedSize == size;

            return GestureDetector(
              onTap: () {
                setState(() {
                  _selectedSize = size;
                  colors =
                      widget.product.stock
                          .where((s) => s.size == _selectedSize)
                          .map((s) => s.color)
                          .toSet()
                          .toList();
                  if (colors.isNotEmpty) {
                    _selectedColor = colors.first;
                  }

                  _productPrice =
                      widget.product.stock
                          .firstWhere(
                            (s) =>
                                s.size == _selectedSize &&
                                s.color == _selectedColor,
                          )
                          .price;
                });
              },
              child: Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 16,
                  vertical: 12,
                ),
                decoration: BoxDecoration(
                  color:
                      isSelected ? const Color(0xFF2196F3) : Colors.transparent,
                  border: Border.all(
                    color:
                        isSelected
                            ? const Color(0xFF2196F3)
                            : Colors.grey[300]!,
                  ),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  size,
                  style: TextStyle(
                    color: isSelected ? Colors.white : Colors.black,
                    fontWeight:
                        isSelected ? FontWeight.bold : FontWeight.normal,
                  ),
                ),
              ),
            );
          }).toList(),
    );
  }
}
