import 'package:flutter/material.dart';
import 'package:mobile_app/pages/checkout.dart';
import 'package:mobile_app/utils/navigator.dart';

class ShoppingCartPage extends StatefulWidget {
  const ShoppingCartPage({super.key});

  @override
  State<ShoppingCartPage> createState() => _ShoppingCartPageState();
}

class _ShoppingCartPageState extends State<ShoppingCartPage> {
  // Exemple de données panier simulées
  List<Map<String, dynamic>> cartItems = [
    {
      'id': 1,
      'name': 'Produit A',
      'price': 29.99,
      'quantity': 2,
      'imageUrl': 'https://via.placeholder.com/150',
    },
    {
      'id': 2,
      'name': 'Produit B',
      'price': 15.50,
      'quantity': 1,
      'imageUrl': 'https://via.placeholder.com/150',
    },
  ];

  double get totalPrice {
    return cartItems.fold(
      0,
      (sum, item) => sum + item['price'] * item['quantity'],
    );
  }

  void _incrementQuantity(int index) {
    setState(() {
      cartItems[index]['quantity']++;
    });
  }

  void _decrementQuantity(int index) {
    setState(() {
      if (cartItems[index]['quantity'] > 1) {
        cartItems[index]['quantity']--;
      }
    });
  }

  void _removeItem(int index) {
    setState(() {
      cartItems.removeAt(index);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Mon Panier'), centerTitle: true),
      body:
          cartItems.isEmpty
              ? Center(
                child: Text(
                  'Votre panier est vide',
                  style: TextStyle(fontSize: 18),
                ),
              )
              : Column(
                children: [
                  Expanded(
                    child: ListView.builder(
                      itemCount: cartItems.length,
                      itemBuilder: (context, index) {
                        final item = cartItems[index];
                        return Card(
                          margin: EdgeInsets.symmetric(
                            horizontal: 12,
                            vertical: 8,
                          ),
                          child: ListTile(
                            // leading: Image.network(
                            //   item['imageUrl'],
                            //   width: 50,
                            //   height: 50,
                            //   fit: BoxFit.cover,
                            // ),
                            title: Text(item['name']),
                            subtitle: Text(
                              "${item['price'].toStringAsFixed(2)} €",
                            ),
                            // trailing: SizedBox(
                            //   width: 100,
                            //   child: Row(
                            //     mainAxisAlignment: MainAxisAlignment.end,
                            //     children: [
                            //       IconButton(
                            //         icon: Icon(Icons.remove_circle_outline),
                            //         onPressed: () => _decrementQuantity(index),
                            //       ),
                            //       Text('${item['quantity']}'),
                            //       IconButton(
                            //         icon: Icon(Icons.add_circle_outline),
                            //         onPressed: () => _incrementQuantity(index),
                            //       ),
                            //       IconButton(
                            //         icon: Icon(
                            //           Icons.delete_forever,
                            //           color: Colors.red,
                            //         ),
                            //         onPressed: () => _removeItem(index),
                            //       ),
                            //     ],
                            //   ),
                            // ),
                          ),
                        );
                      },
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.all(16),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          "Total :",
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          "${totalPrice.toStringAsFixed(2)} €",
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(horizontal: 16),
                    child: SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: () {
                          CustomNavigator.navigateTo(context, CheckoutPage());
                        },
                        style: ElevatedButton.styleFrom(
                          padding: EdgeInsets.symmetric(vertical: 12),
                        ),
                        child: Text(
                          "Passer la commande",
                          style: TextStyle(fontSize: 18),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(height: 20),
                ],
              ),
    );
  }
}
