import 'package:flutter/material.dart';
import 'package:mobile_app/api/api_client.dart';
import 'package:mobile_app/models/order_item.dart';
import 'package:mobile_app/models/product_options.dart';
import 'package:mobile_app/services/cart_service.dart';
import '../../models/cart_model.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({super.key});

  @override
  State<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  final _cartService = CartService();

  CartModel? cart;

  @override
  void initState() {
    super.initState();
    _fetchUserCart();
  }

  void _fetchUserCart() async {
    await _cartService.fetchUserCart().then(
      (cart) => setState(() => this.cart = cart),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mon Panier'),
        actions: [
          cart != null && cart!.orderItems.isNotEmpty
              ? TextButton(
                onPressed: () {
                  _showClearCartDialog(context, cart!);
                },
                child: const Text('Vider', style: TextStyle(color: Colors.red)),
              )
              : const SizedBox.shrink(),
        ],
      ),
      body:
          cart == null || cart!.orderItems.isEmpty
              ? _buildEmptyCart(context)
              : Column(
                children: [
                  Expanded(
                    child: ListView.builder(
                      padding: const EdgeInsets.all(16),
                      itemCount: cart!.orderItems.length,
                      itemBuilder: (context, index) {
                        final item = cart!.orderItems[index];
                        return _buildCartItem(context, item, cart!);
                      },
                    ),
                  ),
                  _buildCartSummary(context, cart!),
                ],
              ),
    );
  }

  Widget _buildEmptyCart(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.shopping_cart_outlined,
            size: 120,
            color: Colors.grey[400],
          ),
          const SizedBox(height: 24),
          Text(
            'Votre panier est vide',
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: Colors.grey[600],
            ),
          ),
          const SizedBox(height: 16),
          Text(
            'Ajoutez des produits pour commencer vos achats',
            style: TextStyle(fontSize: 16, color: Colors.grey[500]),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 32),
          ElevatedButton.icon(
            onPressed: () {
              Navigator.pushReplacementNamed(context, '/home');
            },
            icon: const Icon(Icons.shopping_bag),
            label: const Text('Continuer mes achats'),
            style: ElevatedButton.styleFrom(
              padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCartItem(BuildContext context, OrderItem item, CartModel cart) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            // Image du produit
            Container(
              width: 80,
              height: 80,
              decoration: BoxDecoration(
                color: Colors.grey[100],
                borderRadius: BorderRadius.circular(8),
              ),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: Image.network(
                  "${ApiClient.baseUrl}/products/images/${item.productImage}",
                  fit: BoxFit.cover,
                  errorBuilder: (context, error, stackTrace) {
                    return Container(
                      color: Colors.grey[200],
                      child: Icon(
                        Icons.image_not_supported,
                        color: Colors.grey[400],
                        size: 30,
                      ),
                    );
                  },
                ),
              ),
            ),

            const SizedBox(width: 12),

            // Informations du produit
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    item.productName,
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w600,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 6),

                  // Affichage des options sélectionnées
                  if (_buildSelectedOptionsWidgets(item).isNotEmpty) ...[
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: _buildSelectedOptionsWidgets(item),
                    ),
                    const SizedBox(height: 6),
                  ],

                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '${item.price.toStringAsFixed(2)}€',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF2196F3),
                        ),
                      ),
                      Text(
                        'Sous-total: ${(item.price * item.quantity).toStringAsFixed(2)}€',
                        style: const TextStyle(
                          fontSize: 12,
                          fontWeight: FontWeight.w500,
                          color: Colors.grey,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),

                  // Contrôles de quantité
                  Row(
                    children: [
                      Container(
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.grey[300]!),
                          borderRadius: BorderRadius.circular(6),
                        ),
                        child: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            InkWell(
                              onTap: () async {
                                if (item.quantity > 1) {
                                  bool response = await _cartService
                                      .updateOrderItemQuantityInCart(
                                        item.id,
                                        item.quantity - 1,
                                      );
                                  if (response) {
                                    _fetchUserCart();
                                  }
                                } else {
                                  _showRemoveItemDialog(context, item, cart);
                                }
                              },
                              borderRadius: BorderRadius.circular(6),
                              child: Container(
                                padding: const EdgeInsets.all(6),
                                child: Icon(
                                  item.quantity > 1
                                      ? Icons.remove
                                      : Icons.delete,
                                  size: 16,
                                  color:
                                      item.quantity > 1
                                          ? Colors.grey[600]
                                          : Colors.red,
                                ),
                              ),
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 12,
                              ),
                              child: Text(
                                '${item.quantity}',
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            InkWell(
                              onTap: () async {
                                bool response = await _cartService
                                    .updateOrderItemQuantityInCart(
                                      item.id,
                                      item.quantity + 1,
                                    );

                                if (response) {
                                  _fetchUserCart();
                                }
                              },
                              borderRadius: BorderRadius.circular(6),
                              child: Container(
                                padding: const EdgeInsets.all(6),
                                child: Icon(
                                  Icons.add,
                                  size: 16,
                                  color: Colors.grey[600],
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // Bouton supprimer
            IconButton(
              onPressed: () {
                _showRemoveItemDialog(context, item, cart);
              },
              icon: const Icon(Icons.close, color: Colors.red, size: 20),
            ),
          ],
        ),
      ),
    );
  }

  List<Widget> _buildSelectedOptionsWidgets(OrderItem item) {
    List<Widget> widgets = [];
    final colorHex = ProductOptions.getColorHexValues();

    // Afficher la couleur sélectionnée
    widgets.add(
      Container(
        margin: const EdgeInsets.only(bottom: 2),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 8,
              height: 8,
              decoration: BoxDecoration(
                color: Color(colorHex[item.color] ?? 0xFF000000),
                borderRadius: BorderRadius.circular(2),
                border: Border.all(color: Colors.grey[300]!, width: 0.5),
              ),
            ),
            const SizedBox(width: 4),
            Flexible(
              child: Text(
                'Couleur: ${item.color}',
                style: TextStyle(fontSize: 12, color: Colors.grey[600]),
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );

    // Afficher la taille sélectionnée
    widgets.add(
      Container(
        margin: const EdgeInsets.only(bottom: 2),
        child: Text(
          'Taille: ${item.size}',
          style: TextStyle(fontSize: 12, color: Colors.grey[600]),
          overflow: TextOverflow.ellipsis,
        ),
      ),
    );

    return widgets;
  }

  Widget _buildCartSummary(BuildContext context, CartModel cart) {
    double totalPrice = cart.orderItems
        .map((item) => item.price * item.quantity)
        .reduce((a, b) => a + b);
    return Container(
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
      child: Column(
        children: [
          // Résumé des coûts
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Colors.grey[50],
              borderRadius: BorderRadius.circular(12),
            ),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'Articles (${cart.orderItems.length})',
                      style: const TextStyle(fontSize: 16),
                    ),
                    Text(
                      '${totalPrice.toStringAsFixed(2)}€',
                      style: const TextStyle(fontSize: 16),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Livraison', style: TextStyle(fontSize: 16)),
                    Text(
                      totalPrice >= 50 ? 'Gratuite' : '5,99€',
                      style: TextStyle(
                        fontSize: 16,
                        color: totalPrice >= 50 ? Colors.green : Colors.black,
                        fontWeight:
                            totalPrice >= 50
                                ? FontWeight.bold
                                : FontWeight.normal,
                      ),
                    ),
                  ],
                ),
                if (totalPrice < 50)
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: Text(
                      'Livraison gratuite à partir de 50€',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey[600],
                        fontStyle: FontStyle.italic,
                      ),
                    ),
                  ),
                const Divider(height: 24),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'Total',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    Text(
                      '${(totalPrice + (totalPrice >= 50 ? 0 : 5.99)).toStringAsFixed(2)}€',
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF2196F3),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),

          const SizedBox(height: 16),

          // Boutons d'action
          Row(
            children: [
              Expanded(
                child: OutlinedButton.icon(
                  onPressed: () {
                    Navigator.pushReplacementNamed(context, '/home');
                  },
                  icon: const Icon(Icons.shopping_bag_outlined),
                  label: const Text('Continuer'),
                  style: OutlinedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                    side: const BorderSide(color: Color(0xFF2196F3)),
                    foregroundColor: const Color(0xFF2196F3),
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                flex: 2,
                child: ElevatedButton.icon(
                  onPressed: () {
                    Navigator.pushNamed(context, '/payment');
                  },
                  icon: const Icon(Icons.payment),
                  label: const Text('Passer commande'),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  void _showRemoveItemDialog(
    BuildContext context,
    OrderItem item,
    CartModel cart,
  ) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Supprimer l\'article'),
          content: Text(
            'Voulez-vous supprimer "${item.productName}" de votre panier ?',
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text('Annuler'),
            ),
            TextButton(
              onPressed: () async {
                final response = await _cartService.removeOrderItemFromCart(
                  item.id,
                );
                if (response) {
                  _fetchUserCart();
                  Navigator.of(context).pop();
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text('Article supprimé du panier'),
                      backgroundColor: Colors.orange,
                    ),
                  );
                }
              },
              child: const Text(
                'Supprimer',
                style: TextStyle(color: Colors.red),
              ),
            ),
          ],
        );
      },
    );
  }

  void _showClearCartDialog(BuildContext context, CartModel cart) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Vider le panier'),
          content: const Text(
            'Voulez-vous vraiment vider votre panier ? Cette action est irréversible.',
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text('Annuler'),
            ),
            TextButton(
              onPressed: () async {
                final response = await _cartService.clearUserCart();
                if (response) {
                  _fetchUserCart();
                  Navigator.of(context).pop();
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text('Panier vidé'),
                      backgroundColor: Colors.orange,
                    ),
                  );
                }
              },
              child: const Text('Vider', style: TextStyle(color: Colors.red)),
            ),
          ],
        );
      },
    );
  }
}
