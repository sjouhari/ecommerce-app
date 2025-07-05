import 'package:flutter/material.dart';
import 'package:mobile_app/api/api_client.dart';
import 'package:mobile_app/models/category.dart';
import 'package:mobile_app/models/product.dart';
import 'package:mobile_app/services/cart_service.dart';
import 'package:mobile_app/services/category_service.dart';
import 'package:mobile_app/services/product_service.dart';
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
  CartModel? cart;
  List<Product> _products = [];

  late Future<List<Category>> _categoriesFuture;
  final _categoryService = CategoryService();

  final _productService = ProductService();

  final _cartService = CartService();

  @override
  void initState() {
    super.initState();
    _categoriesFuture = _categoryService.fetchCategories();
    _fetchProducts();
    _fetchUserCart();
  }

  void _fetchProducts() async {
    final products = await _productService.fetchProducts();
    setState(() {
      _products = products;
    });
  }

  void _fetchUserCart() {
    _cartService.fetchUserCart().then(
      (cart) => setState(() => this.cart = cart),
    );
  }

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
      'category': 'Ordinateurs',
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
      'category': 'Téléphones',
    },
    {
      'image': 'assets/images/offre.png',
      'title': 'Offre T-shirts',
      'subtitle': 'Collection de t-shirts tendance à prix réduits',
      'category': 'Vêtements',
    },
  ];

  List<Product> get _filteredProducts {
    List<Product> filtered = _products;

    // Filtrer par catégorie
    if (_selectedCategory.isNotEmpty) {
      filtered =
          filtered
              .where((product) => product.categoryName == _selectedCategory)
              .toList();
    }

    // Filtrer par recherche
    if (_searchQuery.isNotEmpty) {
      filtered =
          filtered
              .where(
                (product) =>
                    product.name.toLowerCase().contains(
                      _searchQuery.toLowerCase(),
                    ) ||
                    product.description.toLowerCase().contains(
                      _searchQuery.toLowerCase(),
                    ),
              )
              .toList();
    }

    // Filtrer par prix
    if (_priceFilter.isNotEmpty) {
      switch (_priceFilter) {
        case 'low':
          filtered =
              filtered
                  .where((product) => product.stock[0].price < 100)
                  .toList();
          break;
        case 'medium':
          filtered =
              filtered
                  .where(
                    (product) =>
                        product.stock[0].price >= 100 &&
                        product.stock[0].price < 500,
                  )
                  .toList();
          break;
        case 'high':
          filtered =
              filtered
                  .where((product) => product.stock[0].price >= 500)
                  .toList();
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
                FutureBuilder<List<Category>>(
                  future: _categoriesFuture,
                  builder: (context, snapshot) {
                    if (snapshot.connectionState == ConnectionState.waiting) {
                      return const Center(child: CircularProgressIndicator());
                    } else if (snapshot.hasError) {
                      return Center(child: Text('Error: ${snapshot.error}'));
                    } else if (snapshot.hasData) {
                      final categories = snapshot.data!;
                      return Column(
                        children:
                            categories.map((category) {
                              return ListTile(
                                title: Text(category.name),
                                onTap: () {
                                  setState(() {
                                    _selectedCategory = category.name;
                                  });
                                  Navigator.pop(context);
                                },
                              );
                            }).toList(),
                      );
                    } else {
                      return const Center(child: Text('No categories found.'));
                    }
                  },
                ),
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
                suffixIcon:
                    _searchQuery.isNotEmpty
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
                contentPadding: const EdgeInsets.symmetric(
                  horizontal: 20,
                  vertical: 16,
                ),
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
                    _buildPriceFilter(
                      '100€ - 500€',
                      'medium',
                      Icons.monetization_on,
                    ),
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
            gradient:
                isSelected
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
                color:
                    isSelected
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
                    targetCategory = 'Ordinateurs';
                    break;
                  case 'assets/images/offre television.png':
                    targetCategory = 'Télévision';
                    break;
                  case 'assets/images/offre telephone.png':
                    targetCategory = 'Téléphones';
                    break;
                  case 'assets/images/offre.png':
                    targetCategory = 'Vetements';
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
          children:
              _promoSlides.asMap().entries.map((entry) {
                return Container(
                  width: 8,
                  height: 8,
                  margin: const EdgeInsets.symmetric(horizontal: 4),
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color:
                        _currentSlideIndex == entry.key
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
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.0),
          child: Row(
            children: [
              Text(
                'Catégories',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              Spacer(),
              _buildCategoryWidget(
                Category(id: 0, name: 'Tous'),
                _selectedCategory == '',
              ),
            ],
          ),
        ),
        const SizedBox(height: 10),
        FutureBuilder<List<Category>>(
          future: _categoriesFuture,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Center(child: Text('Error: ${snapshot.error}'));
            } else if (snapshot.hasData) {
              final categories = snapshot.data!;
              return SizedBox(
                height: 50,
                child: ListView.builder(
                  shrinkWrap: true,
                  scrollDirection: Axis.horizontal,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  itemCount: categories.length,
                  itemBuilder: (context, index) {
                    final category = categories[index];
                    final isSelected =
                        category.name == 'Tous'
                            ? _selectedCategory.isEmpty
                            : _selectedCategory == category.name;

                    return _buildCategoryWidget(category, isSelected);
                  },
                ),
              );
            } else {
              return const Center(child: Text('No categories found.'));
            }
          },
        ),
      ],
    );
  }

  Widget _buildCategoryWidget(Category category, bool isSelected) {
    return GestureDetector(
      onTap: () {
        setState(() {
          if (category.name == 'Tous') {
            _showAllCategories();
          } else {
            setState(() {
              _selectedCategory = isSelected ? '' : category.name;
            });
          }
        });
      },
      child: Container(
        margin: const EdgeInsets.only(right: 12),
        child: AnimatedContainer(
          padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          duration: const Duration(milliseconds: 200),
          decoration: BoxDecoration(
            color: isSelected ? Colors.white : Colors.grey.shade200,
            border: Border.all(
              color: isSelected ? Colors.grey.shade400 : Colors.grey.shade300,
              width: 1.5,
            ),
            borderRadius: BorderRadius.circular(15),
          ),
          child: Text(
            category.name,
            style: TextStyle(
              fontSize: 20,
              fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
              color: isSelected ? Colors.black54 : Colors.black87,
            ),
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ),
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
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              children: [
                // Handle bar
                Container(
                  width: 40,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),

                // Titre
                const Text(
                  'Toutes les catégories',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: Color(0xFF2196F3),
                  ),
                ),

                SizedBox(height: 20),

                // Grid des catégories
                Expanded(
                  child: FutureBuilder(
                    future: _categoriesFuture,
                    builder: (context, snapshot) {
                      if (snapshot.connectionState == ConnectionState.waiting) {
                        return const Center(child: CircularProgressIndicator());
                      } else if (snapshot.hasError) {
                        return Center(child: Text('Error: ${snapshot.error}'));
                      } else if (snapshot.hasData) {
                        final categories = snapshot.data!;
                        return GridView.builder(
                          gridDelegate:
                              const SliverGridDelegateWithFixedCrossAxisCount(
                                crossAxisCount: 2,
                                childAspectRatio: 1.3,
                                crossAxisSpacing: 15,
                                mainAxisSpacing: 15,
                              ),
                          itemCount: categories.length,
                          itemBuilder: (context, index) {
                            final category = categories[index];
                            return GestureDetector(
                              onTap: () {
                                setState(() {
                                  _selectedCategory = category.name;
                                });
                                Navigator.pop(context); // Fermer le modal
                              },
                              child: Container(
                                decoration: BoxDecoration(
                                  color: Colors.grey.shade200,
                                  border: Border.all(
                                    color: Colors.grey.shade300,
                                    width: 1.5,
                                  ),
                                  borderRadius: BorderRadius.circular(20),
                                ),
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    const SizedBox(height: 10),
                                    Text(
                                      category.name,
                                      style: const TextStyle(
                                        color: Colors.black54,
                                        fontSize: 20,
                                        fontWeight: FontWeight.bold,
                                      ),
                                      textAlign: TextAlign.center,
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                    const SizedBox(height: 5),
                                    Text(
                                      '${_getProductCountForCategory(category.name)} produits',
                                      style: const TextStyle(
                                        color: Colors.black87,
                                        fontSize: 16,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            );
                          },
                        );
                      } else {
                        return const Center(
                          child: Text('No categories found.'),
                        );
                      }
                    },
                  ),
                ),

                // Bouton "Tous les produits"
                SizedBox(
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
              ],
            ),
          ),
        );
      },
    );
  }

  int _getProductCountForCategory(String categoryName) {
    return _products
        .where((product) => product.categoryName == categoryName)
        .length;
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
                style: TextStyle(color: Colors.grey[600], fontSize: 14),
              ),
            ],
          ),
          const SizedBox(height: 16),

          filteredProducts.isEmpty
              ? const Center(
                child: Column(
                  children: [
                    Icon(Icons.search_off, size: 80, color: Colors.grey),
                    SizedBox(height: 16),
                    Text(
                      'Aucun produit trouvé',
                      style: TextStyle(fontSize: 18, color: Colors.grey),
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

  Widget _buildProductCard(Product product) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: InkWell(
        onTap: () async {
          await Navigator.pushNamed(
            context,
            '/product-detail',
            arguments: product,
          );
          _fetchProducts();
          _fetchUserCart();
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
                  child: Image.network(
                    "${ApiClient.baseUrl}/products/images/${product.medias[0].url}",
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
                              product.name,
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
                    product.name,
                    style: const TextStyle(
                      fontWeight: FontWeight.w600,
                      fontSize: 14,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 4),
                  Text(
                    '${product.stock[0].price.toStringAsFixed(2)}€',
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
                      onPressed: () async {
                        await Navigator.pushNamed(
                          context,
                          '/product-detail',
                          arguments: product,
                        );
                        _fetchProducts();
                        _fetchUserCart();
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
