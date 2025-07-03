class Media {
  final String url;

  Media({required this.url});

  factory Media.fromJson(Map<String, dynamic> json) {
    return Media(url: json['url']);
  }
}
