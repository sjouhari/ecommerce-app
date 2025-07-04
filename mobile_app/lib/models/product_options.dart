class ProductOptions {
  static const Map<String, List<String>> colors = {
    'AirPods': [
      'Noir',
      'Blanc',
      'Rouge',
      'Bleu',
      'Jaune',
      'Vert',
      'Silver',
      'Gold',
      'Vert ouvert',
      'Gris foncé',
    ],
    'T-shirt': [
      'Noir',
      'Blanc',
      'Rouge',
      'Bleu',
      'Jaune',
      'Vert',
      'Rose',
      'Gris',
    ],
    'Téléphone': ['Noir', 'Blanc', 'Rouge', 'Bleu', 'Vert', 'Rose', 'Violet'],
    'Chaussures': ['Noir', 'Blanc', 'Rouge', 'Bleu', 'Marron', 'Gris'],
  };

  static const Map<String, List<String>> sizes = {
    'T-shirt': ['S', 'M', 'L', 'XL', 'XXL'],
    'Chaussures': [
      '36',
      '37',
      '38',
      '39',
      '40',
      '41',
      '42',
      '43',
      '44',
      '45',
      '46',
    ],
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
      'ROUGE': 0xFFFF0000,
      'VERT': 0xFF00FF00,
      'BLEU': 0xFF0000FF,
      'JAUNE': 0xFFFFFF00,
      'NOIR': 0xFF000000,
      'BLANC': 0xFFFFFFFF,
      'GRIS': 0xFF808080,
      'ROSE': 0xFFFF69B4,
      'ORANGE': 0xFFFFD700,
      'MARRON': 0xFFA0522D,
      'VIOLET': 0xFF8A2BE2,
    };
  }
}
