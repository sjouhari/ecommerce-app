import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/cart_model.dart';

class CartScreen extends StatelessWidget {
  const CartScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mon Panier'),
        actions: [
          Consumer<CartModel>(
            builder: (context, cart, child) {
              return cart.items.isNotEmpty
                  ? TextButton(
                    onPressed: () {
                      _showClearCartDialog(context, cart);
                    },
                    child: const Text(
                      'Vider',
                      style: TextStyle(color: Colors.red),
                    ),
                  )
                  : const SizedBox.shrink();
            },
          ),
        ],
      ),
      body: Consumer<CartModel>(
        builder: (context, cart, child) {
          if (cart.items.isEmpty) {
            return _buildEmptyCart(context);
          }

          return Column(
            children: [
              Expanded(
                child: ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: cart.items.length,
                  itemBuilder: (context, index) {
                    final item = cart.items[index];
                    return _buildCartItem(context, item, cart);
                  },
                ),
              ),
              _buildCartSummary(context, cart),
            ],
          );
        },
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

  Widget _buildCartItem(BuildContext context, CartItem item, CartModel cart) {
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
                child: Image.asset(
                  item.image,
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
                    item.name,
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
                              onTap: () {
                                if (item.quantity > 1) {
                                  cart.updateQuantity(
                                    item.uniqueId,
                                    item.quantity - 1,
                                  );
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
                              onTap: () {
                                cart.updateQuantity(
                                  item.uniqueId,
                                  item.quantity + 1,
                                );
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

  List<Widget> _buildSelectedOptionsWidgets(CartItem item) {
    List<Widget> widgets = [];

    // Afficher la couleur sélectionnée
    if (item.selectedColor != null) {
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
                  color: _getColorFromName(item.selectedColor!),
                  borderRadius: BorderRadius.circular(2),
                  border: Border.all(color: Colors.grey[300]!, width: 0.5),
                ),
              ),
              const SizedBox(width: 4),
              Flexible(
                child: Text(
                  'Couleur: ${item.selectedColor}',
                  style: TextStyle(fontSize: 12, color: Colors.grey[600]),
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
        ),
      );
    }

    // Afficher la taille sélectionnée
    if (item.selectedSize != null) {
      widgets.add(
        Container(
          margin: const EdgeInsets.only(bottom: 2),
          child: Text(
            'Taille: ${item.selectedSize}',
            style: TextStyle(fontSize: 12, color: Colors.grey[600]),
            overflow: TextOverflow.ellipsis,
          ),
        ),
      );
    }

    // Afficher les spécifications sélectionnées
    if (item.selectedSpec != null) {
      widgets.add(
        Container(
          margin: const EdgeInsets.only(bottom: 2),
          child: Text(
            'Écran: ${item.selectedSpec}',
            style: TextStyle(fontSize: 12, color: Colors.grey[600]),
            overflow: TextOverflow.ellipsis,
          ),
        ),
      );
    }

    return widgets;
  }

  Color _getColorFromName(String colorName) {
    final colorMap = {
      'Noir': Colors.black,
      'Blanc': Colors.white,
      'Rouge': Colors.red,
      'Bleu': Colors.blue,
      'Jaune': Colors.yellow,
      'Vert': Colors.green,
      'Rose': Colors.pink,
      'Gris': Colors.grey,
      'Violet': Colors.purple,
      'Marron': Colors.brown,
      'Silver': const Color(0xFFC0C0C0),
      'Gold': const Color(0xFFFFD700),
      'Vert ouvert': const Color(0xFF90EE90),
      'Gris foncé': const Color(0xFF2F2F2F),
    };

    return colorMap[colorName] ?? Colors.grey;
  }

  Widget _buildCartSummary(BuildContext context, CartModel cart) {
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
                      'Articles (${cart.itemCount})',
                      style: const TextStyle(fontSize: 16),
                    ),
                    Text(
                      '${cart.totalPrice.toStringAsFixed(2)}€',
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
                      cart.totalPrice >= 50 ? 'Gratuite' : '5,99€',
                      style: TextStyle(
                        fontSize: 16,
                        color:
                            cart.totalPrice >= 50 ? Colors.green : Colors.black,
                        fontWeight:
                            cart.totalPrice >= 50
                                ? FontWeight.bold
                                : FontWeight.normal,
                      ),
                    ),
                  ],
                ),
                if (cart.totalPrice < 50)
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
                      '${(cart.totalPrice + (cart.totalPrice >= 50 ? 0 : 5.99)).toStringAsFixed(2)}€',
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
    CartItem item,
    CartModel cart,
  ) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Supprimer l\'article'),
          content: Text(
            'Voulez-vous supprimer "${item.name}" de votre panier ?',
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: const Text('Annuler'),
            ),
            TextButton(
              onPressed: () {
                cart.removeItem(item.uniqueId);
                Navigator.of(context).pop();
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Article supprimé du panier'),
                    backgroundColor: Colors.orange,
                  ),
                );
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
              onPressed: () {
                cart.clearCart();
                Navigator.of(context).pop();
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Panier vidé'),
                    backgroundColor: Colors.orange,
                  ),
                );
              },
              child: const Text('Vider', style: TextStyle(color: Colors.red)),
            ),
          ],
        );
      },
    );
  }
}
