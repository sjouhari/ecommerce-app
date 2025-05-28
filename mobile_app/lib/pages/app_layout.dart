import 'package:flutter/material.dart';
import 'package:mobile_app/pages/home.dart';
import 'package:mobile_app/pages/profile.dart';
import 'package:mobile_app/pages/shopping-cart.dart';

class AppLayout extends StatefulWidget {
  const AppLayout({super.key});

  @override
  State<AppLayout> createState() => _AppLayoutState();
}

class _AppLayoutState extends State<AppLayout> {
  int _currentIndex = 0;
  List<Widget> pages = [HomePage(), ShoppingCartPage(), ProfilePage()];

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: pages[_currentIndex],
        bottomNavigationBar: BottomNavigationBar(
          type: BottomNavigationBarType.fixed,
          backgroundColor: Colors.white,
          currentIndex: _currentIndex,
          onTap: (index) {
            setState(() {
              _currentIndex = index;
            });
          },
          iconSize: 25,
          selectedIconTheme: IconThemeData(
            color: const Color.fromARGB(255, 217, 111, 104),
          ),
          unselectedIconTheme: IconThemeData(
            color: const Color.fromARGB(255, 70, 70, 70),
          ),
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.home), label: "Accueil"),
            BottomNavigationBarItem(
              icon: Icon(Icons.shopping_cart_checkout),
              label: "Panier",
            ),
            BottomNavigationBarItem(icon: Icon(Icons.person), label: "Profile"),
          ],
        ),
      ),
    );
  }
}
