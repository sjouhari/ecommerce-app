import 'package:flutter/material.dart';
import 'package:mobile_app/pages/home.dart';

class AppLayout extends StatefulWidget {
  const AppLayout({super.key});

  @override
  State<AppLayout> createState() => _AppLayoutState();
}

class _AppLayoutState extends State<AppLayout> {
  @override
  Widget build(BuildContext context) {
    int currentIndex = 0;

    List<Widget> pages = [HomePage(), HomePage(), HomePage()];

    return SafeArea(
      child: Scaffold(
        body: pages[currentIndex],
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: currentIndex,
          onTap: (index) {
            setState(() {
              currentIndex = index;
            });
          },
          iconSize: 32,
          selectedIconTheme: IconThemeData(
            color: const Color.fromARGB(255, 217, 111, 104),
          ),
          unselectedIconTheme: IconThemeData(
            color: const Color.fromARGB(255, 70, 70, 70),
          ),
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.home), label: "Home"),
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
