import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/cart_model.dart';
import '../../models/product_options.dart';

class ProductDetailScreen extends StatefulWidget {
  final Map<String, dynamic> product;

  const ProductDetailScreen({super.key, required this.product});

  @override
  State<ProductDetailScreen> createState() => _ProductDetailScreenState();
}

class _ProductDetailScreenState extends State<ProductDetailScreen> {
  int _quantity = 1;
  bool _isFavorite = false;
  String? _selectedColor;
  String? _selectedSize;
  String? _selectedSpec;

  @override
  void initState() {
    super.initState();
    // Initialiser les sélections par défaut
    final category = widget.product['category'];
    final colors = ProductOptions.getColorsForCategory(category);
    final sizes = ProductOptions.getSizesForCategory(category); 
    final specs = ProductOptions.getSpecificationsForCategory(category);
    
    if (colors != null && colors.isNotEmpty) {
      _selectedColor = colors.first;
    }
    if (sizes != null && sizes.isNotEmpty) {
      _selectedSize = sizes.first;
    }
    if (specs != null && specs.isNotEmpty) {
      _selectedSpec = specs.first;
    }
  }

  bool get _canAddToCart {
    final category = widget.product['category'];
    final colors = ProductOptions.getColorsForCategory(category);
    final sizes = ProductOptions.getSizesForCategory(category);
    final specs = ProductOptions.getSpecificationsForCategory(category);
    
    // Vérifier que toutes les options requises sont sélectionnées
    if (colors != null && _selectedColor == null) return false;
    if (sizes != null && _selectedSize == null) return false;
    if (specs != null && _selectedSpec == null) return false;
    
    return true;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.product['name']),
        actions: [
          IconButton(
            icon: Icon(
              _isFavorite ? Icons.favorite : Icons.favorite_border,
              color: _isFavorite ? Colors.red : null,
            ),
            onPressed: () {
              setState(() {
                _isFavorite = !_isFavorite;
              });
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(
                    _isFavorite 
                        ? 'Ajouté aux favoris' 
                        : 'Retiré des favoris',
                  ),
                  duration: const Duration(seconds: 1),
                ),
              );
            },
          ),
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
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Image du produit
            Container(
              width: double.infinity,
              height: 300,
              color: Colors.grey[100],
              child: Image.asset(
                widget.product['image'],
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) {
                  return Container(
                    color: Colors.grey[200],
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.image_not_supported,
                          size: 80,
                          color: Colors.grey[400],
                        ),
                        const SizedBox(height: 16),
                        Text(
                          widget.product['name'],
                          style: TextStyle(
                            color: Colors.grey[600],
                            fontSize: 16,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  );
                },
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Nom et catégorie
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          widget.product['name'],
                          style: const TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  
                  // Catégorie
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                    decoration: BoxDecoration(
                      color: const Color(0xFF2196F3).withOpacity(0.1),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Text(
                      widget.product['category'],
                      style: const TextStyle(
                        color: Color(0xFF2196F3),
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ),
                  
                  const SizedBox(height: 16),
                  
                  // Prix
                  Text(
                    '${widget.product['price'].toStringAsFixed(2)}€',
                    style: const TextStyle(
                      fontSize: 32,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF2196F3),
                    ),
                  ),
                  
                  const SizedBox(height: 24),
                  
                  // Options de personnalisation
                  ..._buildProductOptions(),
                  
                  // Description
                  const Text(
                    'Description',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    widget.product['description'],
                    style: TextStyle(
                      fontSize: 16,
                      color: Colors.grey[700],
                      height: 1.5,
                    ),
                  ),
                  
                  const SizedBox(height: 24),
                  
                  // Caractéristiques
                  const Text(
                    'Caractéristiques',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 12),
                  _buildFeaturesList(),
                  
                  const SizedBox(height: 32),
                  
                  // Sélecteur de quantité
                  Row(
                    children: [
                      const Text(
                        'Quantité:',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                      const SizedBox(width: 16),
                      Container(
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.grey[300]!),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Row(
                          children: [
                            IconButton(
                              onPressed: _quantity > 1 ? () {
                                setState(() {
                                  _quantity--;
                                });
                              } : null,
                              icon: const Icon(Icons.remove),
                              iconSize: 20,
                            ),
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 16),
                              child: Text(
                                '$_quantity',
                                style: const TextStyle(
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            IconButton(
                              onPressed: _quantity < 10 ? () {
                                setState(() {
                                  _quantity++;
                                });
                              } : null,
                              icon: const Icon(Icons.add),
                              iconSize: 20,
                            ),
                          ],
                        ),
                      ),
                      const Spacer(),
                      Text(
                        'Total: ${(widget.product['price'] * _quantity).toStringAsFixed(2)}€',
                        style: const TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF2196F3),
                        ),
                      ),
                    ],
                  ),
                  
                  const SizedBox(height: 32),
                ],
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: Container(
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
        child: Row(
          children: [
            Expanded(
              child: OutlinedButton.icon(
                onPressed: _canAddToCart ? () {
                  // Acheter maintenant - aller directement au panier
                  for (int i = 0; i < _quantity; i++) {
                    context.read<CartModel>().addItem(
                      widget.product['id'],
                      widget.product['name'],
                      widget.product['price'],
                      widget.product['image'],
                      selectedColor: _selectedColor,
                      selectedSize: _selectedSize,
                      selectedSpec: _selectedSpec,
                    );
                  }
                  Navigator.pushNamed(context, '/cart');
                } : null,
                icon: const Icon(Icons.flash_on),
                label: const Text('Acheter maintenant'),
                style: OutlinedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  side: BorderSide(
                    color: _canAddToCart ? const Color(0xFF2196F3) : Colors.grey,
                  ),
                  foregroundColor: _canAddToCart ? const Color(0xFF2196F3) : Colors.grey,
                ),
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: ElevatedButton.icon(
                onPressed: _canAddToCart ? () {
                  for (int i = 0; i < _quantity; i++) {
                    context.read<CartModel>().addItem(
                      widget.product['id'],
                      widget.product['name'],
                      widget.product['price'],
                      widget.product['image'],
                      selectedColor: _selectedColor,
                      selectedSize: _selectedSize,
                      selectedSpec: _selectedSpec,
                    );
                  }
                  
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text(
                        '$_quantity x ${widget.product['name']} ajouté(s) au panier',
                      ),
                      duration: const Duration(seconds: 2),
                      backgroundColor: Colors.green,
                      action: SnackBarAction(
                        label: 'Voir panier',
                        textColor: Colors.white,
                        onPressed: () {
                          Navigator.pushNamed(context, '/cart');
                        },
                      ),
                    ),
                  );
                } : null,
                icon: const Icon(Icons.add_shopping_cart),
                label: const Text('Ajouter au panier'),
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  backgroundColor: _canAddToCart ? null : Colors.grey,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  List<Widget> _buildProductOptions() {
    List<Widget> options = [];
    final category = widget.product['category'];
    
    // Sélecteur de couleur  
    final colors = ProductOptions.getColorsForCategory(category);
    if (colors != null) {
      options.addAll([
        const Text(
          'Couleur:',
          style: TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 12),
        _buildColorSelector(colors),
        const SizedBox(height: 24),
      ]);
    }
    
    // Sélecteur de taille
    final sizes = ProductOptions.getSizesForCategory(category);
    if (sizes != null) {
      options.addAll([
        const Text(
          'Taille:',
          style: TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 12),
        _buildSizeSelector(sizes),
        const SizedBox(height: 24),
      ]);
    }
    
    // Sélecteur de spécifications (taille d'écran, etc.)
    final specs = ProductOptions.getSpecificationsForCategory(category);
    if (specs != null) {
      String label = 'Taille d\'écran:';
      if (category == 'Ordinateur portable') {
        label = 'Taille d\'écran:';
      } else if (category == 'Télévision') {
        label = 'Taille d\'écran:';
      }
      
      options.addAll([
        Text(
          label,
          style: const TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
        const SizedBox(height: 12),
        _buildSpecSelector(specs),
        const SizedBox(height: 24),
      ]);
    }
    
    return options;
  }

  Widget _buildColorSelector(List<String> colors) {
    final colorHex = ProductOptions.getColorHexValues();
    
    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children: colors.map((color) {
        final isSelected = _selectedColor == color;
        final colorValue = colorHex[color] ?? 0xFF000000;
        
        return GestureDetector(
          onTap: () {
            setState(() {
              _selectedColor = color;
            });
          },
          child: Container(
            width: 35,
            height: 35,
            decoration: BoxDecoration(
              color: Color(colorValue),
              borderRadius: BorderRadius.circular(6),
              border: Border.all(
                color: isSelected ? const Color(0xFF2196F3) : Colors.grey[300]!,
                width: isSelected ? 2.5 : 1,
              ),
              boxShadow: isSelected ? [
                BoxShadow(
                  color: const Color(0xFF2196F3).withOpacity(0.3),
                  spreadRadius: 1,
                  blurRadius: 6,
                )
              ] : null,
            ),
            child: color == 'Blanc' ? Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(6),
                border: Border.all(color: Colors.grey[300]!, width: 1),
              ),
            ) : null,
          ),
        );
      }).toList(),
    );
  }

  Widget _buildSizeSelector(List<String> sizes) {
    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children: sizes.map((size) {
        final isSelected = _selectedSize == size;
        
        return GestureDetector(
          onTap: () {
            setState(() {
              _selectedSize = size;
            });
          },
          child: Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            decoration: BoxDecoration(
              color: isSelected ? const Color(0xFF2196F3) : Colors.transparent,
              border: Border.all(
                color: isSelected ? const Color(0xFF2196F3) : Colors.grey[300]!,
              ),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Text(
              size,
              style: TextStyle(
                color: isSelected ? Colors.white : Colors.black,
                fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
              ),
            ),
          ),
        );
      }).toList(),
    );
  }

  Widget _buildSpecSelector(List<String> specs) {
    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children: specs.map((spec) {
        final isSelected = _selectedSpec == spec;
        
        return GestureDetector(
          onTap: () {
            setState(() {
              _selectedSpec = spec;
            });
          },
          child: Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            decoration: BoxDecoration(
              color: isSelected ? const Color(0xFF2196F3) : Colors.transparent,
              border: Border.all(
                color: isSelected ? const Color(0xFF2196F3) : Colors.grey[300]!,
              ),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Text(
              spec,
              style: TextStyle(
                color: isSelected ? Colors.white : Colors.black,
                fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
              ),
            ),
          ),
        );
      }).toList(),
    );
  }

  Widget _buildFeaturesList() {
    // Caractéristiques basées sur la catégorie
    List<String> features = [];
    
    switch (widget.product['category']) {
      case 'Téléphone':
        features = [
          '• Écran haute résolution',
          '• Appareil photo professionnel',
          '• Batterie longue durée',
          '• Résistant à l\'eau',
          '• Charge rapide',
        ];
        break;
      case 'Ordinateur portable':
        features = [
          '• Processeur haute performance',
          '• Écran Full HD',
          '• SSD rapide',
          '• Clavier rétroéclairé',
          '• Autonomie 8+ heures',
        ];
        break;
      case 'T-shirt':
        features = [
          '• 100% coton premium',
          '• Coupe moderne',
          '• Lavable en machine',
          '• Tailles disponibles: S à XXL',
          '• Couleurs variées',
        ];
        break;
      case 'AirPods':
        features = [
          '• Son haute qualité',
          '• Réduction de bruit active',
          '• Autonomie 6+ heures',
          '• Charge sans fil',
          '• Résistant à la transpiration',
        ];
        break;
      case 'Télévision':
        features = [
          '• Résolution 4K UHD',
          '• Smart TV intégrée',
          '• HDR10+ support',
          '• Multiples ports HDMI',
          '• Design ultra-fin',
        ];
        break;
      case 'Chaussures':
        features = [
          '• Semelle amortissante',
          '• Matériaux respirants',
          '• Design ergonomique',
          '• Tailles 36 à 46',
          '• Garantie 2 ans',
        ];
        break;
      default:
        features = [
          '• Qualité premium',
          '• Garantie constructeur',
          '• Livraison rapide',
          '• Service client 24/7',
        ];
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: features.map((feature) => Padding(
        padding: const EdgeInsets.only(bottom: 8),
        child: Text(
          feature,
          style: TextStyle(
            fontSize: 14,
            color: Colors.grey[700],
          ),
        ),
      )).toList(),
    );
  }
} 