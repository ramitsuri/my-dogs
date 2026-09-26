package com.ramitsuri.mydogs.domain

val jsonStr = """
            {
  "sizeClasses": {
    "small": {
      "label": "Small",
      "weightRangeLb": "0 - 20 lbs",
      "defaultLifespanYears": 14.0,
      "lifeStageChart": [
        {
          "dogAgeYears": 0.5,
          "humanYears": 10.0
        },
        {
          "dogAgeYears": 1.0,
          "humanYears": 15.0
        },
        {
          "dogAgeYears": 2.0,
          "humanYears": 24.0
        },
        {
          "dogAgeYears": 3.0,
          "humanYears": 28.0
        },
        {
          "dogAgeYears": 5.0,
          "humanYears": 36.0
        },
        {
          "dogAgeYears": 10.0,
          "humanYears": 56.0
        },
        {
          "dogAgeYears": 15.0,
          "humanYears": 76.0
        }
      ]
    },
    "medium": {
      "label": "Medium",
      "weightRangeLb": "21 - 50 lbs",
      "defaultLifespanYears": 13.0,
      "lifeStageChart": [
        {
          "dogAgeYears": 0.5,
          "humanYears": 10.0
        },
        {
          "dogAgeYears": 1.0,
          "humanYears": 15.0
        },
        {
          "dogAgeYears": 2.0,
          "humanYears": 24.0
        },
        {
          "dogAgeYears": 3.0,
          "humanYears": 29.0
        },
        {
          "dogAgeYears": 5.0,
          "humanYears": 40.0
        },
        {
          "dogAgeYears": 10.0,
          "humanYears": 66.0
        },
        {
          "dogAgeYears": 14.0,
          "humanYears": 84.0
        }
      ]
    },
    "large": {
      "label": "Large",
      "weightRangeLb": "51 - 90 lbs",
      "defaultLifespanYears": 11.0,
      "lifeStageChart": [
        {
          "dogAgeYears": 0.5,
          "humanYears": 10.0
        },
        {
          "dogAgeYears": 1.0,
          "humanYears": 15.0
        },
        {
          "dogAgeYears": 2.0,
          "humanYears": 24.0
        },
        {
          "dogAgeYears": 3.0,
          "humanYears": 30.0
        },
        {
          "dogAgeYears": 5.0,
          "humanYears": 42.0
        },
        {
          "dogAgeYears": 10.0,
          "humanYears": 70.0
        },
        {
          "dogAgeYears": 12.0,
          "humanYears": 85.0
        }
      ]
    },
    "giant": {
      "label": "Giant",
      "weightRangeLb": "91+ lbs",
      "defaultLifespanYears": 9.5,
      "lifeStageChart": [
        {
          "dogAgeYears": 0.5,
          "humanYears": 10.0
        },
        {
          "dogAgeYears": 1.0,
          "humanYears": 14.0
        },
        {
          "dogAgeYears": 2.0,
          "humanYears": 22.0
        },
        {
          "dogAgeYears": 3.0,
          "humanYears": 31.0
        },
        {
          "dogAgeYears": 5.0,
          "humanYears": 45.0
        },
        {
          "dogAgeYears": 8.0,
          "humanYears": 65.0
        },
        {
          "dogAgeYears": 10.0,
          "humanYears": 80.0
        }
      ]
    }
  },
  "breeds": [
    {
      "name": "Chihuahua",
      "lifespanYears": 15,
      "sizeClass": "small"
    },
    {
      "name": "Yorkshire Terrier",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Pomeranian",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Toy Poodle",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Maltese",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Papillon",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Pug",
      "lifespanYears": 12,
      "sizeClass": "small"
    },
    {
      "name": "Shih Tzu",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Chinese Crested",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Italian Greyhound",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Japanese Chin",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Havanese",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Brussels Griffon",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Miniature Pinscher",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Affenpinscher",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Pekingese",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Miniature Dachshund",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Boston Terrier",
      "lifespanYears": 12,
      "sizeClass": "small"
    },
    {
      "name": "Cavalier King Charles Spaniel",
      "lifespanYears": 12,
      "sizeClass": "small"
    },
    {
      "name": "Miniature Schnauzer",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "West Highland White Terrier",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Cairn Terrier",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Jack Russell Terrier",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Bichon Frise",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Scottish Terrier",
      "lifespanYears": 12,
      "sizeClass": "small"
    },
    {
      "name": "Rat Terrier",
      "lifespanYears": 15,
      "sizeClass": "small"
    },
    {
      "name": "Toy Fox Terrier",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Border Terrier",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Lhasa Apso",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "Norfolk Terrier",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Silky Terrier",
      "lifespanYears": 14,
      "sizeClass": "small"
    },
    {
      "name": "French Bulldog",
      "lifespanYears": 10,
      "sizeClass": "small"
    },
    {
      "name": "Shiba Inu",
      "lifespanYears": 15,
      "sizeClass": "small"
    },
    {
      "name": "Standard Dachshund",
      "lifespanYears": 13,
      "sizeClass": "small"
    },
    {
      "name": "Beagle",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Pembroke Welsh Corgi",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "Cardigan Welsh Corgi",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Cocker Spaniel",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "Border Collie",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Australian Shepherd",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Brittany",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Basset Hound",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "English Springer Spaniel",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Shetland Sheepdog",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "American Staffordshire Terrier",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "American Pit Bull Terrier",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "Chow Chow",
      "lifespanYears": 11,
      "sizeClass": "medium"
    },
    {
      "name": "Standard Schnauzer",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Siberian Husky",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Portuguese Water Dog",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "Vizsla",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Keeshond",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Finnish Spitz",
      "lifespanYears": 14,
      "sizeClass": "medium"
    },
    {
      "name": "Norwegian Elkhound",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Samoyed",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "Basenji",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Whippet",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "English Bulldog",
      "lifespanYears": 9,
      "sizeClass": "medium"
    },
    {
      "name": "Bull Terrier",
      "lifespanYears": 12,
      "sizeClass": "medium"
    },
    {
      "name": "Staffordshire Bull Terrier",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Australian Cattle Dog",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Welsh Springer Spaniel",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "American Eskimo Dog",
      "lifespanYears": 13,
      "sizeClass": "medium"
    },
    {
      "name": "Xoloitzcuintli",
      "lifespanYears": 14,
      "sizeClass": "medium"
    },
    {
      "name": "Labrador Retriever",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "Golden Retriever",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "German Shepherd",
      "lifespanYears": 10,
      "sizeClass": "large"
    },
    {
      "name": "Boxer",
      "lifespanYears": 10,
      "sizeClass": "large"
    },
    {
      "name": "Doberman Pinscher",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Rottweiler",
      "lifespanYears": 9,
      "sizeClass": "large"
    },
    {
      "name": "Weimaraner",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Bloodhound",
      "lifespanYears": 10,
      "sizeClass": "large"
    },
    {
      "name": "Standard Poodle",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "Belgian Malinois",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "Akita",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Rhodesian Ridgeback",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Bernese Mountain Dog",
      "lifespanYears": 8,
      "sizeClass": "large"
    },
    {
      "name": "Alaskan Malamute",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Chesapeake Bay Retriever",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "American Bulldog",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "German Shorthaired Pointer",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "Gordon Setter",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Irish Setter",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "English Setter",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "Collie",
      "lifespanYears": 13,
      "sizeClass": "large"
    },
    {
      "name": "Old English Sheepdog",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Airedale Terrier",
      "lifespanYears": 11,
      "sizeClass": "large"
    },
    {
      "name": "Catahoula Leopard Dog",
      "lifespanYears": 12,
      "sizeClass": "large"
    },
    {
      "name": "Great Dane",
      "lifespanYears": 8,
      "sizeClass": "giant"
    },
    {
      "name": "English Mastiff",
      "lifespanYears": 7,
      "sizeClass": "giant"
    },
    {
      "name": "Saint Bernard",
      "lifespanYears": 8,
      "sizeClass": "giant"
    },
    {
      "name": "Newfoundland",
      "lifespanYears": 9,
      "sizeClass": "giant"
    },
    {
      "name": "Irish Wolfhound",
      "lifespanYears": 7,
      "sizeClass": "giant"
    },
    {
      "name": "Great Pyrenees",
      "lifespanYears": 10,
      "sizeClass": "giant"
    },
    {
      "name": "Leonberger",
      "lifespanYears": 8,
      "sizeClass": "giant"
    },
    {
      "name": "Tibetan Mastiff",
      "lifespanYears": 10,
      "sizeClass": "giant"
    },
    {
      "name": "Cane Corso",
      "lifespanYears": 10,
      "sizeClass": "giant"
    },
    {
      "name": "Greater Swiss Mountain Dog",
      "lifespanYears": 8,
      "sizeClass": "giant"
    },
    {
      "name": "Anatolian Shepherd",
      "lifespanYears": 11,
      "sizeClass": "giant"
    },
    {
      "name": "Dogue de Bordeaux",
      "lifespanYears": 6,
      "sizeClass": "giant"
    },
    {
      "name": "Bullmastiff",
      "lifespanYears": 8,
      "sizeClass": "giant"
    },
    {
      "name": "Neapolitan Mastiff",
      "lifespanYears": 7,
      "sizeClass": "giant"
    },
    {
      "name": "Scottish Deerhound",
      "lifespanYears": 8,
      "sizeClass": "giant"
    },
    {
      "name": "Kuvasz",
      "lifespanYears": 10,
      "sizeClass": "giant"
    },
    {
      "name": "Landseer",
      "lifespanYears": 9,
      "sizeClass": "giant"
    }
  ]
}
            """.trimIndent()
