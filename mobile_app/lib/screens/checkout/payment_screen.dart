import 'package:flutter/material.dart';
import 'package:mobile_app/api/api_client.dart';
import 'package:mobile_app/models/address.dart';
import 'package:mobile_app/models/order_request.dart';
import 'package:mobile_app/models/user.dart';
import 'package:mobile_app/services/address_service.dart';
import 'package:mobile_app/services/cart_service.dart';
import 'package:mobile_app/services/order_service.dart';
import '../../models/cart_model.dart';

class PaymentScreen extends StatefulWidget {
  const PaymentScreen({super.key});

  @override
  State<PaymentScreen> createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {
  String _selectedPaymentMethod = 'CASH';
  String _selectedAddressId = '';
  final _formKey = GlobalKey<FormState>();
  final _firstNameController = TextEditingController();
  final _lastNameController = TextEditingController();
  final _addressController = TextEditingController();
  final _cityController = TextEditingController();
  final _countryController = TextEditingController();
  final _postalCodeController = TextEditingController();
  final _phoneController = TextEditingController();
  final _chequeNumberController = TextEditingController();
  final _banqueNameController = TextEditingController();
  bool _isProcessing = false;

  CartModel? cart;
  final _cartService = CartService();

  List<Address>? addresses;
  final _addressService = AddressService();

  final _orderService = OrderService();

  @override
  void initState() {
    super.initState();
    _fetchUserCart();
    _fetchUserDeliveryAddresses();
  }

  void _fetchUserCart() async {
    await _cartService.fetchUserCart().then(
      (cart) => setState(() => this.cart = cart),
    );
  }

  void _fetchUserDeliveryAddresses() async {
    await _addressService.fetchUserAddresses().then((addresses) {
      setState(() {
        this.addresses = addresses;
        if (addresses.isNotEmpty) {
          _selectedAddressId = addresses.first.id.toString();
        }
      });
    });
  }

  @override
  void dispose() {
    _firstNameController.dispose();
    _lastNameController.dispose();
    _countryController.dispose();
    _chequeNumberController.dispose();
    _banqueNameController.dispose();
    _addressController.dispose();
    _cityController.dispose();
    _postalCodeController.dispose();
    _phoneController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Paiement')),
      body:
          cart == null || cart!.orderItems.isEmpty
              ? _buildEmptyCart(context)
              : SingleChildScrollView(
                padding: const EdgeInsets.all(16),
                child: Form(
                  key: _formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildOrderSummary(cart!),
                      const SizedBox(height: 24),
                      _buildAddressesSection(),
                      const SizedBox(height: 24),
                      _buildPaymentMethods(),
                      const SizedBox(height: 32),
                      _buildConfirmButton(cart!),
                    ],
                  ),
                ),
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
          const SizedBox(height: 32),
          ElevatedButton.icon(
            onPressed: () {
              Navigator.pushReplacementNamed(context, '/home');
            },
            icon: const Icon(Icons.shopping_bag),
            label: const Text('Retour aux achats'),
          ),
        ],
      ),
    );
  }

  Widget _buildOrderSummary(CartModel cart) {
    double totalPrice = cart.orderItems.fold(
      0,
      (total, item) => total + item.price * item.quantity,
    );
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Résumé de la commande',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),

            // Liste des articles
            ...cart.orderItems.map(
              (item) => Padding(
                padding: const EdgeInsets.only(bottom: 12),
                child: Row(
                  children: [
                    Container(
                      width: 50,
                      height: 50,
                      decoration: BoxDecoration(
                        color: Colors.grey[100],
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: Image.network(
                          "${ApiClient.baseUrl}/products/images/${item.productImage}",
                          fit: BoxFit.cover,
                          errorBuilder: (context, error, stackTrace) {
                            return Icon(
                              Icons.image_not_supported,
                              color: Colors.grey[400],
                              size: 20,
                            );
                          },
                        ),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            item.productName,
                            style: const TextStyle(fontWeight: FontWeight.w500),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                          ),
                          Text(
                            'Quantité: ${item.quantity}',
                            style: TextStyle(
                              color: Colors.grey[600],
                              fontSize: 12,
                            ),
                          ),
                        ],
                      ),
                    ),
                    Text(
                      '${(item.price * item.quantity).toStringAsFixed(2)}€',
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF2196F3),
                      ),
                    ),
                  ],
                ),
              ),
            ),

            const Divider(),

            // Totaux
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('Sous-total:'),
                Text('${totalPrice.toStringAsFixed(2)}€'),
              ],
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('Livraison:'),
                Text(
                  totalPrice >= 50 ? 'Gratuite' : '5,99€',
                  style: TextStyle(
                    color: totalPrice >= 50 ? Colors.green : Colors.black,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  'Total:',
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
                Text(
                  '${(totalPrice + (totalPrice >= 50 ? 0 : 5.99)).toStringAsFixed(2)}€',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: Color(0xFF2196F3),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDeliveryForm() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Adresse de livraison',
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 16),

        Row(
          children: [
            Expanded(
              flex: 1,
              child: TextFormField(
                controller: _lastNameController,
                decoration: const InputDecoration(
                  labelText: 'Nom',
                  prefixIcon: Icon(Icons.person),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer votre nom';
                  }
                  return null;
                },
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              flex: 1,
              child: TextFormField(
                controller: _firstNameController,
                decoration: const InputDecoration(
                  labelText: 'Prénom',
                  prefixIcon: Icon(Icons.person),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer votre prénom';
                  }
                  return null;
                },
              ),
            ),
          ],
        ),

        const SizedBox(height: 16),

        TextFormField(
          controller: _addressController,
          decoration: const InputDecoration(
            labelText: 'Adresse',
            prefixIcon: Icon(Icons.home),
          ),
          validator: (value) {
            if (value == null || value.isEmpty) {
              return 'Veuillez entrer votre adresse';
            }
            return null;
          },
        ),
        const SizedBox(height: 16),

        Row(
          children: [
            Expanded(
              child: TextFormField(
                controller: _cityController,
                decoration: const InputDecoration(
                  labelText: 'Ville',
                  prefixIcon: Icon(Icons.location_city),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer votre ville';
                  }
                  return null;
                },
              ),
            ),
            const SizedBox(width: 16),
            Expanded(
              child: TextFormField(
                controller: _countryController,
                decoration: const InputDecoration(
                  labelText: 'Pays',
                  prefixIcon: Icon(Icons.flag),
                ),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Veuillez entrer votre pays';
                  }
                  return null;
                },
              ),
            ),
          ],
        ),
        const SizedBox(height: 16),

        TextFormField(
          controller: _postalCodeController,
          decoration: const InputDecoration(
            labelText: 'Code postal',
            prefixIcon: Icon(Icons.location_pin),
          ),
          keyboardType: TextInputType.number,
          validator: (value) {
            if (value == null || value.isEmpty) {
              return 'Code postal requis';
            }
            if (value.length != 5) {
              return 'Code postal invalide';
            }
            return null;
          },
        ),

        const SizedBox(height: 16),

        TextFormField(
          controller: _phoneController,
          decoration: const InputDecoration(
            labelText: 'Téléphone',
            prefixIcon: Icon(Icons.phone),
          ),
          keyboardType: TextInputType.phone,
          validator: (value) {
            if (value == null || value.isEmpty) {
              return 'Veuillez entrer votre numéro de téléphone';
            }
            return null;
          },
        ),
      ],
    );
  }

  Widget _buildAddressesSection() {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Adresse de livraison',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),

            addresses != null && addresses!.isNotEmpty
                ? ListView.builder(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemCount: addresses?.length,
                  itemBuilder:
                      (context, index) => _buildAddressItem(addresses![index]),
                )
                : const Center(child: Text('Aucune adresse disponible')),

            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color:
                      _selectedAddressId == ''
                          ? const Color(0xFF2196F3)
                          : Colors.grey[300]!,
                  width: 2,
                ),
                borderRadius: BorderRadius.circular(12),
              ),
              child: RadioListTile<String>(
                value: '',
                groupValue: _selectedAddressId,
                onChanged: (value) {
                  setState(() {
                    _selectedAddressId = '';
                  });
                },
                title: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Créer une nouvelle adresse',
                      style: TextStyle(fontWeight: FontWeight.w500),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 16),

            _selectedAddressId == '' ? _buildDeliveryForm() : Container(),
          ],
        ),
      ),
    );
  }

  Widget _buildAddressItem(Address address) {
    return Container(
      decoration: BoxDecoration(
        border: Border.all(
          color:
              _selectedAddressId == address.id.toString()
                  ? const Color(0xFF2196F3)
                  : Colors.grey[300]!,
          width: 2,
        ),
        borderRadius: BorderRadius.circular(12),
      ),
      child: RadioListTile<String>(
        value: address.id.toString(),
        groupValue: _selectedAddressId,
        onChanged: (value) {
          setState(() {
            _selectedAddressId = value!;
          });
        },
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              address.deliveryAddress,
              style: TextStyle(fontWeight: FontWeight.w500),
              overflow: TextOverflow.ellipsis,
            ),
            Text(
              '${address.firstName} ${address.lastName}, ${address.phone}',
              style: TextStyle(fontSize: 12),
            ),
            Text(
              '${address.postalCode}, ${address.city}, ${address.country}',
              style: TextStyle(fontSize: 12),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildPaymentMethods() {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Mode de paiement',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),

            // CASH ON DELIVERY
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color:
                      _selectedPaymentMethod == 'CASH'
                          ? const Color(0xFF2196F3)
                          : Colors.grey[300]!,
                  width: 2,
                ),
                borderRadius: BorderRadius.circular(12),
              ),
              child: RadioListTile<String>(
                value: 'CASH',
                groupValue: _selectedPaymentMethod,
                onChanged: (value) {
                  setState(() {
                    _selectedPaymentMethod = value!;
                  });
                },
                title: const Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.account_balance, color: Color(0xFF2196F3)),
                    SizedBox(width: 8),
                    Flexible(
                      child: Text(
                        'Payement à la livraison',
                        style: TextStyle(fontWeight: FontWeight.w500),
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                  ],
                ),
                subtitle: const Text(
                  'Payer au moment de la livraison',
                  style: TextStyle(fontSize: 12),
                ),
              ),
            ),

            const SizedBox(height: 12),

            // Chèque
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  color:
                      _selectedPaymentMethod == 'CHEQUE'
                          ? const Color(0xFF2196F3)
                          : Colors.grey[300]!,
                  width: 2,
                ),
                borderRadius: BorderRadius.circular(12),
              ),
              child: RadioListTile<String>(
                value: 'CHEQUE',
                groupValue: _selectedPaymentMethod,
                onChanged: (value) {
                  setState(() {
                    _selectedPaymentMethod = value!;
                  });
                },
                title: const Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(Icons.receipt_long, color: Color(0xFF2196F3)),
                    SizedBox(width: 8),
                    Flexible(
                      child: Text(
                        'Paiement par chèque',
                        style: TextStyle(fontWeight: FontWeight.w500),
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                  ],
                ),
                subtitle: const Text(
                  'Payer par chèque lors de la livraison',
                  style: TextStyle(fontSize: 12),
                ),
              ),
            ),

            if (_selectedPaymentMethod.isNotEmpty &&
                _selectedPaymentMethod == 'CHEQUE') ...[
              const SizedBox(height: 16),
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.blue[50],
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(color: Colors.blue[200]!),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Informations pour le chèque:',
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF2196F3),
                      ),
                    ),
                    const SizedBox(height: 8),
                    TextFormField(
                      controller: _chequeNumberController,
                      decoration: const InputDecoration(
                        labelText: 'Numéro de chèque',
                        prefixIcon: Icon(Icons.person),
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Veuillez entrer numero de chèque';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 8),
                    TextFormField(
                      controller: _banqueNameController,
                      decoration: const InputDecoration(
                        labelText: 'Nom de la banque',
                        prefixIcon: Icon(Icons.person),
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Veuillez entrer votre banque';
                        }
                        return null;
                      },
                    ),
                  ],
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildConfirmButton(CartModel cart) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton.icon(
        onPressed:
            _selectedPaymentMethod.isEmpty || _isProcessing
                ? null
                : () => _processOrder(cart),
        icon:
            _isProcessing
                ? const SizedBox(
                  width: 20,
                  height: 20,
                  child: CircularProgressIndicator(
                    color: Colors.white,
                    strokeWidth: 2,
                  ),
                )
                : const Icon(Icons.check_circle),
        label: Text(
          _isProcessing ? 'Traitement en cours...' : 'Confirmer la commande',
        ),
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 16),
          textStyle: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
      ),
    );
  }

  Future<void> _processOrder(CartModel cart) async {
    if (!_formKey.currentState!.validate()) return;
    User? user = await ApiClient.getCurrentUser();
    if (_selectedPaymentMethod.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Veuillez sélectionner un mode de paiement'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    setState(() {
      _isProcessing = true;
    });

    if (_selectedAddressId.isEmpty) {
      Address address = await _addressService.addAddress(
        Address(
          id: 0,
          userId: user!.id,
          firstName: _firstNameController.text.trim(),
          lastName: _lastNameController.text.trim(),
          phone: _phoneController.text.trim(),
          deliveryAddress: _addressController.text.trim(),
          country: _countryController.text.trim(),
          city: _cityController.text.trim(),
          postalCode: _postalCodeController.text.trim(),
        ),
      );

      await _addressService.fetchUserAddresses().then((addresses) {
        setState(() {
          this.addresses = addresses;
          if (addresses.isNotEmpty) {
            _selectedAddressId = address.id.toString();
          }
        });
      });
    }

    await _orderService.placeOrder(
      OrderRequest(
        userId: user!.id,
        deliveryAddressId: int.parse(_selectedAddressId),
        orderItemsIds: cart.orderItems.map((item) => item.id).toList(),
        paymentMethod: _selectedPaymentMethod,
        chequeNumber: _chequeNumberController.text.trim(),
        bankName: _banqueNameController.text.trim(),
      ),
    );

    if (mounted) {
      setState(() {
        _isProcessing = false;
      });

      // Afficher la confirmation
      _showOrderConfirmation(context, cart);
    }
  }

  void _showOrderConfirmation(BuildContext context, CartModel cart) {
    double totalPrice = cart.orderItems
        .map((item) => item.price * item.quantity)
        .reduce((a, b) => a + b);
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(Icons.check_circle, color: Colors.green, size: 24),
              SizedBox(width: 8),
              Flexible(
                child: Text(
                  'Commande confirmée !',
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Total: ${(totalPrice + (totalPrice >= 50 ? 0 : 5.99)).toStringAsFixed(2)}€',
              ),
              const SizedBox(height: 8),
              Text(
                'Mode de paiement: ${_selectedPaymentMethod == 'virement' ? 'Virement bancaire' : 'Chèque'}',
              ),
              const SizedBox(height: 16),
              const Text(
                'Vous recevrez un email de confirmation avec tous les détails.',
                style: TextStyle(fontSize: 12, color: Colors.grey),
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Fermer le dialog
                Navigator.pushNamedAndRemoveUntil(
                  context,
                  '/home',
                  (route) => false,
                ); // Retourner à l'accueil
              },
              child: const Text('Continuer mes achats'),
            ),
          ],
        );
      },
    );
  }
}
