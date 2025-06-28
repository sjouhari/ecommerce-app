class ProductOptions {
  static const Map<String, List<String>> colors = {
    'AirPods': ['Noir', 'Blanc', 'Rouge', 'Bleu', 'Jaune', 'Vert', 'Silver', 'Gold', 'Vert ouvert', 'Gris foncé'],
    'T-shirt': ['Noir', 'Blanc', 'Rouge', 'Bleu', 'Jaune', 'Vert', 'Rose', 'Gris'],
    'Téléphone': ['Noir', 'Blanc', 'Rouge', 'Bleu', 'Vert', 'Rose', 'Violet'],
    'Chaussures': ['Noir', 'Blanc', 'Rouge', 'Bleu', 'Marron', 'Gris'],
  };

  static const Map<String, List<String>> sizes = {
    'T-shirt': ['S', 'M', 'L', 'XL', 'XXL'],
    'Chaussures': ['36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46'],
  };

  static const Map<String, List<String>> specifications = {
    'Télévision': ['32"', '40"', '43"', '50"', '55"', '65"'],
    'Ordinateur portable': ['13"', '14"', '15.6"', '17"'],
  };

  static List<String>? getColorsForCategory(String category) {
    return colors[category];
  }

  static List<String>? getSizesForCategory(String category) {
    return sizes[category];
  }

  static List<String>? getSpecificationsForCategory(String category) {
    return specifications[category];
  }

  static bool hasOptions(String category) {
    return colors.containsKey(category) || 
           sizes.containsKey(category) || 
           specifications.containsKey(category);
  }

  static Map<String, int> getColorHexValues() {
    return {
      'Noir': 0xFF000000,
      'Blanc': 0xFFFFFFFF,
      'Rouge': 0xFFFF0000,
      'Bleu': 0xFF0000FF,
      'Jaune': 0xFFFFFF00,
      'Vert': 0xFF00FF00,
      'Rose': 0xFFFF69B4,
      'Gris': 0xFF808080,
      'Violet': 0xFF8A2BE2,
      'Marron': 0xFFA0522D,
      'Silver': 0xFFC0C0C0,
      'Gold': 0xFFFFD700,
      'Vert ouvert': 0xFF90EE90,
      'Gris foncé': 0xFF2F2F2F,
    };
  }
} 