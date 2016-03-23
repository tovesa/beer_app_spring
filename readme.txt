sudo /bin/systemctl start elasticsearch
sudo /bin/systemctl status elasticsearch
sudo /bin/systemctl start kibana --> does not work
sudo /bin/systemctl status kibana

nohup /opt/kibana/bin/kibana &

# Kibana stuff
curl -XPUT 'http://localhost:9200/.kibana' -d '{
    "index.mapper.dynamic" : "true"
}'

curl -XPUT 'http://localhost:9200/beer-ratings' -d '{
"mappings": {
    "rating": {
      "properties": {
        "date": {
          "type": "date" 
        }
      }
    }
  }
}'


# sense

GET _cat/indices?v

GET beer-rating/_mapping/rating

GET _search
{
  "query": {
    "match_all": {}
  }
}

PUT beer-rating

DELETE beer-rating

PUT /_cluster/settings
{
    "transient" : {
        "logger._root" : "DEBUG"
    }
}

GET beer-rating/rating/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "_type": "rating" } },
        { "match": { "name": "Svaneke Skøre Elg" } }
      ]
    }
  }
}

GET beer-rating/rating/_search
{
  "query" : {
    "term" : {
      "name" : "Svaneke Skøre Elg"
    }
  }
}

GET beer-rating/rating/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "_type": "rating" } },
        { "match": { "ratingPlace": "Pirkkala" } }
      ]
    }
  }
}

PUT beer-rating
{
  "mappings": {
    "rating": {
      "properties": {
        "appearance": {
          "type": "long"
        },
        "aroma": {
          "type": "long"
        },
        "bbe": {
          "type": "string"
        },
        "brewery": {
          "type": "string"
        },
        "brewInfo": {
          "type": "string"
        },
        "country": {
          "type": "string"
        },
        "name": {
          "type": "string"
        },
        "notes": {
          "type": "string"
        },
        "overall": {
          "type": "long"
        },
        "pack": {
          "type": "string"
        },
        "palate": {
          "type": "long"
        },
        "purchasingDate": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "purchasingPlace": {
          "type": "string"
        },
        "ratingDate": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "ratingPlace": {
          "type": "string"
        },
        "rbId": {
          "type": "long"
        },
        "taste": {
          "type": "long"
        }
      }
    }
  }
}