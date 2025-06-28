import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:carousel_slider/carousel_slider.dart';
import '../../models/cart_model.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final TextEditingController _searchController = TextEditingController();
  String _selectedCategory = '';
  String _searchQuery = '';
  String _priceFilter = '';
  int _currentSlideIndex = 0;

  // Catégories disponibles
  final List<Map<String, dynamic>> _categories = [
    {
      'name': 'Téléphone',
      'icon': Icons.phone_android,
      'color': const Color(0xFF4CAF50),
    },
    {
      'name': 'Ordinateur portable',
      'icon': Icons.laptop,
      'color': const Color(0xFF2196F3),
    },
    {
      'name': 'T-shirt',
      'icon': Icons.checkroom,
      'color': const Color(0xFFFF9800),
    },
    {
      'name': 'AirPods',
      'icon': Icons.headphones,
      'color': const Color(0xFF9C27B0),
    },
    {
      'name': 'Télévision',
      'icon': Icons.tv,
      'color': const Color(0xFFF44336),
    },
    {
      'name': 'Chaussures',
      'icon': Icons.sports_baseball,
      'color': const Color(0xFF607D8B),
    },
  ];

  // Images du slideshow promotionnel
  final List<Map<String, String>> _promoSlides = [
    {
      'image': 'assets/images/offre aipods.png',
      'title': 'Offre AirPods',
      'subtitle': 'Écouteurs sans fil à prix réduit',
      'category': 'AirPods',
    },
    {
      'image': 'assets/images/offre pc.png',
      'title': 'Offre PC Portable',
      'subtitle': 'Ordinateurs portables en promotion',
      'category': 'Ordinateur portable',
    },
    {
      'image': 'assets/images/offre television.png',
      'title': 'Offre Télévision',
      'subtitle': 'TV Smart à prix exceptionnel',
      'category': 'Télévision',
    },
    {
      'image': 'assets/images/offre telephone.png',
      'title': 'Offre Téléphone',
      'subtitle': 'Smartphones dernière génération à prix réduit',
      'category': 'Téléphone',
    },
    {
      'image': 'assets/images/offre.png',
      'title': 'Offre T-shirts',
      'subtitle': 'Collection de t-shirts tendance à prix réduits',
      'category': 'T-shirt',
    },
  ];

  // Produits disponibles
  final List<Map<String, dynamic>> _products = [
    {
      'id': '1',
      'name': 'iPhone 14 Pro Blanc',
      'price': 1099.99,
      'category': 'Téléphone',
      'image': 'assets/images/iPhone-14-Pro white.jpg',
      'description': 'Smartphone Apple iPhone 14 Pro avec écran Super Retina XDR',
    },
    {
      'id': '2',
      'name': 'PC Portable HP 15" i7',
      'price': 849.99,
      'category': 'Ordinateur portable',
      'image': 'assets/images/pc portable hp elet book 1 1 3.jpg',
      'description': 'PC Portable HP 15 pouces avec processeur i7',
    },
    {
      'id': '3',
      'name': 'T-shirt Rouge Premium',
      'price': 29.99,
      'category': 'T-shirt',
      'image': 'assets/images/t-shirt-rouge-.png',
      'description': 'T-shirt rouge en coton premium',
    },
    {
      'id': '4',
      'name': 'AirPods Apple Verts',
      'price': 189.99,
      'category': 'AirPods',
      'image': 'assets/images/aple airpods green.jpg',
      'description': 'Écouteurs sans fil AirPods avec réduction de bruit',
    },
    {
      'id': '5',
      'name': 'Samsung TV 32" Smart',
      'price': 299.99,
      'category': 'Télévision',
      'image': 'assets/images/tv-samsung-32 smart.jpg',
      'description': 'Téléviseur Samsung 32 pouces Smart TV',
    },
    {
      'id': '6',
      'name': 'Samsung Galaxy Watch',
      'price': 249.99,
      'category': 'Accessoires',
      'image': 'assets/images/images galaxy.jpg',
      'description': 'Montre connectée Samsung Galaxy Watch avec suivi fitness',
    },
    {
      'id': '7',
      'name': 'Samsung Galaxy S21',
      'price': 699.99,
      'category': 'Téléphone',
      'image': 'assets/images/s21 gray.jpg',
      'description': 'Smartphone Samsung Galaxy S21 Ultra',
    },
    {
      'id': '8',
      'name': 'PC Dell Portable 14"',
      'price': 749.99,
      'category': 'Ordinateur portable',
      'image': 'assets/images/pc-portable-dell-14-n075l549014emea.jpg',
      'description': 'Ordinateur portable Dell 14 pouces performant',
    },
    {
      'id': '9',
      'name': 'T-shirt Gris Mode',
      'price': 34.99,
      'category': 'T-shirt',
      'image': 'assets/images/t-shirt gray.png',
      'description': 'T-shirt gris moderne et confortable',
    },
    {
      'id': '10',
      'name': 'AirPods Silver',
      'price': 179.99,
      'category': 'AirPods',
      'image': 'assets/images/apple-airpods- silver.jpg',
      'description': 'Écouteurs AirPods couleur argent premium',
    },
    {
      'id': '11',
      'name': 'Samsung TV 43"',
      'price': 449.99,
      'category': 'Télévision',
      'image': 'assets/images/Samsung-43.jpg',
      'description': 'Téléviseur Samsung 43 pouces 4K Smart TV',
    },
    {
      'id': '12',
      'name': 'T-shirt Pack Mode',
      'price': 79.99,
      'category': 'T-shirt',
      'image': 'assets/images/lot-de-4-mode-tee-shirt-homme-imprime-col-arrondi.webp',
      'description': 'Pack de 4 t-shirts mode avec col arrondi',
    },
  ];

  List<Map<String, dynamic>> get _filteredProducts {
    List<Map<String, dynamic>> filtered = _products;

    // Filtrer par catégorie
    if (_selectedCategory.isNotEmpty) {
      filtered = filtered.where((product) => 
        product['category'] == _selectedCategory).toList();
    }

    // Filtrer par recherche
    if (_searchQuery.isNotEmpty) {
      filtered = filtered.where((product) =>
        product['name'].toLowerCase().contains(_searchQuery.toLowerCase()) ||
        product['description'].toLowerCase().contains(_searchQuery.toLowerCase())).toList();
    }

    // Filtrer par prix
    if (_priceFilter.isNotEmpty) {
      switch (_priceFilter) {
        case 'low':
          filtered = filtered.where((product) => product['price'] < 100).toList();
          break;
        case 'medium':
          filtered = filtered.where((product) => 
            product['price'] >= 100 && product['price'] < 500).toList();
          break;
        case 'high':
          filtered = filtered.where((product) => product['price'] >= 500).toList();
          break;
      }
    }

    return filtered;
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _buildAppBar(),
      drawer: _buildDrawer(),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildSearchSection(),
            _buildPromoSlideshow(),
            const SizedBox(height: 20),
            _buildCategoriesSection(),
            const SizedBox(height: 20),
            _buildProductsSection(),
          ],
        ),
      ),
    );
  }

  AppBar _buildAppBar() {
    return AppBar(
      title: Image.asset(
        'assets/images/logo.png',
        height: 40,
        fit: BoxFit.contain,
        errorBuilder: (context, error, stackTrace) {
          // Fallback au titre texte si l'image ne charge pas
          return const Text(
            'E-Shop',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: Color(0xFF2196F3),
            ),
          );
        },
      ),
      centerTitle: true,
      actions: [
        Consumer<CartModel>(
          builder: (context, cart, child) {
            return Stack(
              children: [
                IconButton(
                  icon: const Icon(Icons.shopping_cart_outlined),
                  onPressed: () {
                    Navigator.pushNamed(context, '/cart');
                  },
                ),
                if (cart.itemCount > 0)
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
                        '${cart.itemCount}',
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
            );
          },
        ),
        IconButton(
          icon: const Icon(Icons.notifications_outlined),
          onPressed: () {},
        ),
      ],
    );
  }

  Widget _buildDrawer() {
    return Drawer(
      child: Column(
        children: [
          ListTile(
            leading: const Icon(Icons.home),
            title: const Text('Accueil'),
            onTap: () {
              Navigator.pop(context); // Fermer le drawer
              // Réinitialiser les filtres pour afficher tous les produits
              setState(() {
                _selectedCategory = '';
                _searchQuery = '';
                _priceFilter = '';
                _searchController.clear();
              });
            },
          ),
          const Divider(),
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                const Padding(
                  padding: EdgeInsets.all(16.0),
                  child: Text(
                    'Catégories',
                    style: TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Colors.grey,
                    ),
                  ),
                ),
                ..._categories.map((category) => ListTile(
                  leading: Icon(category['icon'], color: category['color']),
                  title: Text(category['name']),
                  onTap: () {
                    setState(() {
                      _selectedCategory = category['name'];
                    });
                    Navigator.pop(context);
                  },
                )),
                const Divider(),
                ListTile(
                  leading: const Icon(Icons.clear_all),
                  title: const Text('Tous les produits'),
                  onTap: () {
                    setState(() {
                      _selectedCategory = '';
                    });
                    Navigator.pop(context);
                  },
                ),
              ],
            ),
          ),
          const Divider(),
          ListTile(
            leading: const Icon(Icons.logout, color: Colors.red),
            title: const Text(
              'Déconnexion',
              style: TextStyle(color: Colors.red),
            ),
            onTap: () {
              Navigator.pushReplacementNamed(context, '/login');
            },
          ),
        ],
      ),
    );
  }

  Widget _buildSearchSection() {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        children: [
          // Barre de recherche
          Container(
            decoration: BoxDecoration(
              boxShadow: [
                BoxShadow(
                  color: Colors.grey.withOpacity(0.1),
                  spreadRadius: 1,
                  blurRadius: 8,
                  offset: const Offset(0, 2),
                ),
              ],
            ),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Rechercher par nom ou description...',
                prefixIcon: const Icon(Icons.search, color: Color(0xFF2196F3)),
                suffixIcon: _searchQuery.isNotEmpty
                    ? IconButton(
                        icon: const Icon(Icons.clear, color: Colors.grey),
                        onPressed: () {
                          _searchController.clear();
                          setState(() {
                            _searchQuery = '';
                          });
                        },
                      )
                    : null,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(15),
                  borderSide: BorderSide.none,
                ),
                filled: true,
                fillColor: Colors.white,
                contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
              ),
              onChanged: (value) {
                setState(() {
                  _searchQuery = value;
                });
              },
            ),
          ),
          const SizedBox(height: 20),
          
          // Filtres de prix modernes
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Filtrer par prix',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w600,
                  color: Colors.black87,
                ),
              ),
              const SizedBox(height: 12),
              SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  children: [
                    _buildPriceFilter('Tous', '', Icons.all_inclusive),
                    _buildPriceFilter('< 100€', 'low', Icons.attach_money),
                    _buildPriceFilter('100€ - 500€', 'medium', Icons.monetization_on),
                    _buildPriceFilter('> 500€', 'high', Icons.diamond),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildPriceFilter(String label, String value, IconData icon) {
    final isSelected = _priceFilter == value;
    return Padding(
      padding: const EdgeInsets.only(right: 12.0),
      child: GestureDetector(
        onTap: () {
          setState(() {
            _priceFilter = _priceFilter == value ? '' : value;
          });
        },
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeInOut,
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
          decoration: BoxDecoration(
            gradient: isSelected 
                ? const LinearGradient(
                    colors: [Color(0xFF2196F3), Color(0xFF21CBF3)],
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                  )
                : null,
            color: isSelected ? null : Colors.white,
            borderRadius: BorderRadius.circular(25),
            border: Border.all(
              color: isSelected ? Colors.transparent : Colors.grey.shade300,
              width: 1.5,
            ),
            boxShadow: [
              BoxShadow(
                color: isSelected 
                    ? const Color(0xFF2196F3).withOpacity(0.3)
                    : Colors.grey.withOpacity(0.1),
                spreadRadius: isSelected ? 2 : 1,
                blurRadius: isSelected ? 8 : 4,
                offset: const Offset(0, 2),
              ),
            ],
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                icon,
                size: 18,
                color: isSelected ? Colors.white : const Color(0xFF2196F3),
              ),
              const SizedBox(width: 8),
              Text(
                label,
                style: TextStyle(
                  color: isSelected ? Colors.white : Colors.black87,
                  fontWeight: isSelected ? FontWeight.w600 : FontWeight.w500,
                  fontSize: 14,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildPromoSlideshow() {
    return Column(
      children: [
        CarouselSlider.builder(
          itemCount: _promoSlides.length,
          itemBuilder: (context, index, realIndex) {
            final slide = _promoSlides[index];
            return GestureDetector(
              onTap: () {
                // Mapping direct des images vers les catégories
                String targetCategory = '';
                
                switch (slide['image']) {
                  case 'assets/images/offre aipods.png':
                    targetCategory = 'AirPods';
                    break;
                  case 'assets/images/offre pc.png':
                    targetCategory = 'Ordinateur portable';
                    break;
                  case 'assets/images/offre television.png':
                    targetCategory = 'Télévision';
                    break;
                  case 'assets/images/offre telephone.png':
                    targetCategory = 'Téléphone';
                    break;
                  case 'assets/images/offre.png':
                    targetCategory = 'T-shirt';
                    break;
                }
                
                if (targetCategory.isNotEmpty) {
                  setState(() {
                    _selectedCategory = targetCategory;
                  });
                  
                  // Feedback visuel amélioré
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Row(
                        children: const [
                          Icon(Icons.check_circle, color: Colors.white),
                          SizedBox(width: 8),
                          Expanded(
                            child: Text('Catégorie sélectionnée avec succès'),
                          ),
                        ],
                      ),
                      duration: const Duration(seconds: 2),
                      backgroundColor: Colors.green,
                      behavior: SnackBarBehavior.floating,
                    ),
                  );
                }
              },
              child: Container(
                margin: const EdgeInsets.symmetric(horizontal: 8),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(15),
                  gradient: const LinearGradient(
                    colors: [Color(0xFF2196F3), Color(0xFF21CBF3)],
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.grey.withOpacity(0.3),
                      spreadRadius: 2,
                      blurRadius: 8,
                      offset: const Offset(0, 3),
                    ),
                  ],
                ),
                child: Stack(
                  children: [
                    ClipRRect(
                      borderRadius: BorderRadius.circular(15),
                      child: Image.asset(
                        slide['image']!,
                        width: double.infinity,
                        height: 200,
                        fit: BoxFit.cover,
                        errorBuilder: (context, error, stackTrace) {
                          return Container(
                            width: double.infinity,
                            height: 200,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(15),
                              gradient: const LinearGradient(
                                colors: [Color(0xFF2196F3), Color(0xFF21CBF3)],
                              ),
                            ),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                const Icon(
                                  Icons.shopping_bag,
                                  size: 60,
                                  color: Colors.white,
                                ),
                                const SizedBox(height: 10),
                                Text(
                                  slide['title']!,
                                  style: const TextStyle(
                                    color: Colors.white,
                                    fontSize: 24,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                Text(
                                  slide['subtitle']!,
                                  style: const TextStyle(
                                    color: Colors.white70,
                                    fontSize: 16,
                                  ),
                                ),
                              ],
                            ),
                          );
                        },
                      ),
                    ),
                    // Indicateur visuel de clic
                    Positioned(
                      top: 10,
                      right: 10,
                      child: Container(
                        padding: const EdgeInsets.all(8),
                        decoration: BoxDecoration(
                          color: Colors.black.withOpacity(0.5),
                          borderRadius: BorderRadius.circular(20),
                        ),
                        child: const Icon(
                          Icons.touch_app,
                          color: Colors.white,
                          size: 20,
                        ),
                      ),
                    ),
                    // Overlay avec texte "Cliquez pour voir les produits"
                    Positioned(
                      bottom: 0,
                      left: 0,
                      right: 0,
                      child: Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          borderRadius: const BorderRadius.only(
                            bottomLeft: Radius.circular(15),
                            bottomRight: Radius.circular(15),
                          ),
                          gradient: LinearGradient(
                            begin: Alignment.topCenter,
                            end: Alignment.bottomCenter,
                            colors: [
                              Colors.transparent,
                              Colors.black.withOpacity(0.7),
                            ],
                          ),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              slide['title']!,
                              style: const TextStyle(
                                color: Colors.white,
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              _getClickMessageForSlide(slide['image']!),
                              style: const TextStyle(
                                color: Colors.white70,
                                fontSize: 12,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
          options: CarouselOptions(
            height: 200,
            autoPlay: true,
            autoPlayInterval: const Duration(seconds: 4),
            enlargeCenterPage: true,
            onPageChanged: (index, reason) {
              setState(() {
                _currentSlideIndex = index;
              });
            },
          ),
        ),
        const SizedBox(height: 10),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: _promoSlides.asMap().entries.map((entry) {
            return Container(
              width: 8,
              height: 8,
              margin: const EdgeInsets.symmetric(horizontal: 4),
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: _currentSlideIndex == entry.key
                    ? const Color(0xFF2196F3)
                    : Colors.grey[300],
              ),
            );
          }).toList(),
        ),
      ],
    );
  }

  Widget _buildCategoriesSection() {
    // Catégories principales à afficher
    final List<Map<String, dynamic>> displayedCategories = [
      {
        'name': 'Téléphone',
        'icon': Icons.phone_android,
        'color': const Color(0xFF4CAF50),
      },
      {
        'name': 'Ordinateur portable',
        'icon': Icons.laptop,
        'color': const Color(0xFF2196F3),
      },
      {
        'name': 'AirPods',
        'icon': Icons.headphones,
        'color': const Color(0xFF9C27B0),
      },
      {
        'name': 'T-shirt',
        'icon': Icons.checkroom,
        'color': const Color(0xFFFF9800),
      },
      {
        'name': 'Tous',
        'icon': Icons.apps,
        'color': const Color(0xFF607D8B),
      },
    ];

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.0),
          child: Text(
            'Catégories',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        const SizedBox(height: 10),
        SizedBox(
          height: 85,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            itemCount: displayedCategories.length,
            itemBuilder: (context, index) {
              final category = displayedCategories[index];
              final isSelected = category['name'] == 'Tous' 
                  ? _selectedCategory.isEmpty 
                  : _selectedCategory == category['name'];
              
              return GestureDetector(
                onTap: () {
                  if (category['name'] == 'Tous') {
                    _showAllCategories();
                  } else {
                    setState(() {
                      _selectedCategory = isSelected ? '' : category['name'];
                    });
                  }
                },
                child: Container(
                  width: 75,
                  margin: const EdgeInsets.only(right: 12),
                  child: Column(
                    children: [
                      AnimatedContainer(
                        duration: const Duration(milliseconds: 200),
                        width: 50,
                        height: 50,
                        decoration: BoxDecoration(
                          color: isSelected 
                              ? category['color'] 
                              : category['color'].withOpacity(0.2),
                          borderRadius: BorderRadius.circular(15),
                          boxShadow: isSelected ? [
                            BoxShadow(
                              color: category['color'].withOpacity(0.4),
                              spreadRadius: 2,
                              blurRadius: 8,
                              offset: const Offset(0, 2),
                            ),
                          ] : [],
                        ),
                        child: Icon(
                          category['icon'],
                          size: 25,
                          color: isSelected ? Colors.white : category['color'],
                        ),
                      ),
                      const SizedBox(height: 6),
                      Text(
                        category['name'],
                        style: TextStyle(
                          fontSize: 10,
                          fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                          color: isSelected ? category['color'] : Colors.black87,
                        ),
                        textAlign: TextAlign.center,
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  void _showAllCategories() {
    showModalBottomSheet(
      context: context,
      backgroundColor: Colors.transparent,
      isScrollControlled: true,
      builder: (BuildContext context) {
        return Container(
          height: MediaQuery.of(context).size.height * 0.7,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(25),
              topRight: Radius.circular(25),
            ),
          ),
          child: Column(
            children: [
              // Handle bar
              Container(
                margin: const EdgeInsets.only(top: 10),
                width: 40,
                height: 4,
                decoration: BoxDecoration(
                  color: Colors.grey[300],
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
              
              // Titre
              const Padding(
                padding: EdgeInsets.all(20.0),
                child: Text(
                  'Toutes les catégories',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: Color(0xFF2196F3),
                  ),
                ),
              ),
              
              // Grid des catégories
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 20.0),
                  child: GridView.builder(
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 1.2,
                      crossAxisSpacing: 15,
                      mainAxisSpacing: 15,
                    ),
                    itemCount: _categories.length,
                    itemBuilder: (context, index) {
                      final category = _categories[index];
                      return GestureDetector(
                        onTap: () {
                          setState(() {
                            _selectedCategory = category['name'];
                          });
                          Navigator.pop(context); // Fermer le modal
                        },
                        child: Container(
                          decoration: BoxDecoration(
                            gradient: LinearGradient(
                              colors: [
                                category['color'],
                                category['color'].withOpacity(0.7),
                              ],
                              begin: Alignment.topLeft,
                              end: Alignment.bottomRight,
                            ),
                            borderRadius: BorderRadius.circular(20),
                            boxShadow: [
                              BoxShadow(
                                color: category['color'].withOpacity(0.3),
                                spreadRadius: 2,
                                blurRadius: 8,
                                offset: const Offset(0, 4),
                              ),
                            ],
                          ),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(
                                category['icon'],
                                size: 40,
                                color: Colors.white,
                              ),
                              const SizedBox(height: 10),
                              Text(
                                category['name'],
                                style: const TextStyle(
                                  color: Colors.white,
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                ),
                                textAlign: TextAlign.center,
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                              ),
                              const SizedBox(height: 5),
                              Text(
                                '${_getProductCountForCategory(category['name'])} produits',
                                style: const TextStyle(
                                  color: Colors.white70,
                                  fontSize: 12,
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ),
              
              // Bouton "Tous les produits"
              Padding(
                padding: const EdgeInsets.all(20.0),
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton.icon(
                    onPressed: () {
                      setState(() {
                        _selectedCategory = '';
                      });
                      Navigator.pop(context);
                    },
                    icon: const Icon(Icons.clear_all),
                    label: const Text('Voir tous les produits'),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 15),
                      backgroundColor: const Color(0xFF607D8B),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(15),
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  int _getProductCountForCategory(String categoryName) {
    return _products.where((product) => product['category'] == categoryName).length;
  }

  Widget _buildProductsSection() {
    final filteredProducts = _filteredProducts;
    
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                _selectedCategory.isEmpty 
                    ? 'Tous les produits' 
                    : _selectedCategory,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Text(
                '${filteredProducts.length} produit(s)',
                style: TextStyle(
                  color: Colors.grey[600],
                  fontSize: 14,
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
          
          filteredProducts.isEmpty
              ? const Center(
                  child: Column(
                    children: [
                      Icon(
                        Icons.search_off,
                        size: 80,
                        color: Colors.grey,
                      ),
                      SizedBox(height: 16),
                      Text(
                        'Aucun produit trouvé',
                        style: TextStyle(
                          fontSize: 18,
                          color: Colors.grey,
                        ),
                      ),
                    ],
                  ),
                )
              : GridView.builder(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 2,
                    childAspectRatio: 0.75,
                    crossAxisSpacing: 12,
                    mainAxisSpacing: 12,
                  ),
                  itemCount: filteredProducts.length,
                  itemBuilder: (context, index) {
                    return _buildProductCard(filteredProducts[index]);
                  },
                ),
        ],
      ),
    );
  }

  Widget _buildProductCard(Map<String, dynamic> product) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      child: InkWell(
        onTap: () {
          Navigator.pushNamed(
            context, 
            '/product-detail',
            arguments: product,
          );
        },
        borderRadius: BorderRadius.circular(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: Container(
                width: double.infinity,
                decoration: BoxDecoration(
                  color: Colors.grey[100],
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(12),
                    topRight: Radius.circular(12),
                  ),
                ),
                child: ClipRRect(
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(12),
                    topRight: Radius.circular(12),
                  ),
                  child: Image.asset(
                    product['image'],
                    fit: BoxFit.cover,
                    errorBuilder: (context, error, stackTrace) {
                      return Container(
                        color: Colors.grey[200],
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(
                              Icons.image_not_supported,
                              size: 40,
                              color: Colors.grey[400],
                            ),
                            const SizedBox(height: 8),
                            Text(
                              product['name'],
                              style: TextStyle(
                                color: Colors.grey[600],
                                fontSize: 12,
                              ),
                              textAlign: TextAlign.center,
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    product['name'],
                    style: const TextStyle(
                      fontWeight: FontWeight.w600,
                      fontSize: 14,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 4),
                  Text(
                    '${product['price'].toStringAsFixed(2)}€',
                    style: const TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 16,
                      color: Color(0xFF2196F3),
                    ),
                  ),
                  const SizedBox(height: 8),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      onPressed: () {
                        context.read<CartModel>().addItem(
                          product['id'],
                          product['name'],
                          product['price'],
                          product['image'],
                        );
                        
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text('${product['name']} ajouté au panier'),
                            duration: const Duration(seconds: 2),
                            backgroundColor: Colors.green,
                          ),
                        );
                      },
                      icon: const Icon(Icons.add_shopping_cart, size: 16),
                      label: const Text(
                        'Ajouter',
                        style: TextStyle(fontSize: 12),
                      ),
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 8),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  String _getClickMessageForSlide(String imagePath) {
    switch (imagePath) {
      case 'assets/images/offre aipods.png':
        return 'Cliquez pour voir les AirPods';
      case 'assets/images/offre pc.png':
        return 'Cliquez pour voir les ordinateurs portables';
      case 'assets/images/offre television.png':
        return 'Cliquez pour voir les télévisions';
      case 'assets/images/offre telephone.png':
        return 'Cliquez pour voir les téléphones';
      case 'assets/images/offre.png':
        return 'Cliquez pour voir les t-shirts';
      default:
        return 'Cliquez pour voir les produits';
    }
  }
} 