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