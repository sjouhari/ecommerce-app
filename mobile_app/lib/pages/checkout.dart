import 'package:flutter/material.dart';

class CheckoutPage extends StatefulWidget {
  const CheckoutPage({super.key});

  @override
  State<CheckoutPage> createState() => _CheckoutPageState();
}

class _CheckoutPageState extends State<CheckoutPage> {
  List<Map<String, dynamic>> cartItems = [
    {'name': 'Produit A', 'price': 29.99, 'quantity': 2},
    {'name': 'Produit B', 'price': 15.50, 'quantity': 1},
  ];

  String selectedDeliveryMethod = "Standard";
  String selectedAddress = "12 Rue Exemple, Paris";

  double get totalPrice {
    return cartItems.fold(
      0,
      (sum, item) => sum + item['price'] * item['quantity'],
    );
  }

  void _placeOrder() {
    // TODO: Envoyer les infos au backend (produits, livraison, adresse)
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text("Commande envoyée avec succès !")));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Passer la commande'), centerTitle: true),
      body: ListView(
        padding: EdgeInsets.all(16),
        children: [
          Text(
            "Récapitulatif",
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          ...cartItems.map(
            (item) => ListTile(
              title: Text(item['name']),
              subtitle: Text(
                "${item['quantity']} x ${item['price'].toStringAsFixed(2)} €",
              ),
              trailing: Text(
                "${(item['price'] * item['quantity']).toStringAsFixed(2)} €",
              ),
            ),
          ),
          Divider(height: 32),
          Text(
            "Méthode de livraison",
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          SizedBox(height: 10),
          DropdownButton<String>(
            value: selectedDeliveryMethod,
            onChanged: (value) {
              if (value != null) {
                setState(() {
                  selectedDeliveryMethod = value;
                });
              }
            },
            items:
                ["Standard", "Express", "Retrait en magasin"]
                    .map(
                      (method) =>
                          DropdownMenuItem(value: method, child: Text(method)),
                    )
                    .toList(),
          ),
          SizedBox(height: 20),
          Text(
            "Adresse de livraison",
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          SizedBox(height: 10),
          TextFormField(
            initialValue: selectedAddress,
            onChanged: (value) {
              selectedAddress = value;
            },
            decoration: InputDecoration(
              border: OutlineInputBorder(),
              labelText: "Adresse",
            ),
          ),
          Divider(height: 32),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                "Total :",
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              Text(
                "${totalPrice.toStringAsFixed(2)} €",
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
            ],
          ),
          SizedBox(height: 20),
          ElevatedButton(
            onPressed: _placeOrder,
            style: ElevatedButton.styleFrom(
              padding: EdgeInsets.symmetric(vertical: 14),
              backgroundColor: Colors.green,
            ),
            child: Text(
              "Confirmer la commande",
              style: TextStyle(fontSize: 18),
            ),
          ),
        ],
      ),
    );
  }
}
