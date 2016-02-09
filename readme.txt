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