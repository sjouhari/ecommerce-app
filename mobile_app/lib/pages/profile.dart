import 'package:flutter/material.dart';
import 'package:mobile_app/pages/orders.dart';
import 'package:mobile_app/utils/navigator.dart';

class ProfilePage extends StatelessWidget {
  const ProfilePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Profil"),
        centerTitle: true,
        backgroundColor: Colors.orange.shade400,
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // Profile Info
          Center(
            child: Column(
              children: [
                CircleAvatar(
                  radius: 40,
                  // backgroundImage: NetworkImage(
                  //   "https://via.placeholder.com/150",
                  // ),
                ),
                const SizedBox(height: 10),
                const Text(
                  "John Doe",
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 5),
                const Text(
                  "john.doe@example.com",
                  style: TextStyle(color: Colors.grey),
                ),
              ],
            ),
          ),
          const SizedBox(height: 30),

          // Action List
          profileItem(
            icon: Icons.shopping_bag,
            title: "Mes commandes",
            onTap: () {
              CustomNavigator.navigateTo(context, OrdersPage());
            },
          ),
          profileItem(
            icon: Icons.location_on,
            title: "Mes adresses",
            onTap: () {},
          ),
          profileItem(icon: Icons.settings, title: "Paramètres", onTap: () {}),
          profileItem(
            icon: Icons.lock,
            title: "Changer le mot de passe",
            onTap: () {},
          ),
          profileItem(
            icon: Icons.help_outline,
            title: "Aide & FAQ",
            onTap: () {},
          ),

          const SizedBox(height: 30),
          ElevatedButton.icon(
            onPressed: () {
              // TODO: Logout logic
            },
            icon: const Icon(Icons.logout),
            label: const Text("Se déconnecter"),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.redAccent,
              foregroundColor: Colors.white,
              padding: const EdgeInsets.symmetric(vertical: 12),
            ),
          ),
        ],
      ),
    );
  }

  Widget profileItem({
    required IconData icon,
    required String title,
    required VoidCallback onTap,
  }) {
    return ListTile(
      leading: Icon(icon, color: Colors.orange.shade400),
      title: Text(title),
      trailing: const Icon(Icons.arrow_forward_ios, size: 16),
      onTap: onTap,
    );
  }
}
