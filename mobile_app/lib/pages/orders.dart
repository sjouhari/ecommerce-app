import 'package:flutter/material.dart';

class OrdersPage extends StatelessWidget {
  const OrdersPage({super.key});

  @override
  Widget build(BuildContext context) {
    final orders = [
      {
        'id': '#CMD1234',
        'date': '27 mai 2025',
        'status': 'Livré',
        'total': 59.99,
      },
      {
        'id': '#CMD1235',
        'date': '25 mai 2025',
        'status': 'En cours',
        'total': 89.50,
      },
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text("Mes Commandes"),
        backgroundColor: Colors.orange.shade400,
        centerTitle: true,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: orders.length,
        itemBuilder: (context, index) {
          final order = orders[index];
          return Card(
            margin: const EdgeInsets.only(bottom: 16),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            child: ListTile(
              contentPadding: const EdgeInsets.all(16),
              title: Text(
                "Commande ${order['id']}",
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              subtitle: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text("Date : ${order['date']}"),
                  Text("Statut : ${order['status']}"),
                  Text("Total : ${order['total']} €"),
                ],
              ),
              trailing: ElevatedButton(
                onPressed: () {},
                child: const Text("Détails"),
              ),
            ),
          );
        },
      ),
    );
  }
}
