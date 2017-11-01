For run this application you must perform following steps:
1. Install Docker;
2. Download this files https://drive.google.com/file/d/0B501BYuA_0ZxV3FXN2Z3ZWxPX0E/view
3. Run create-docker-image.py;
4. Run start-docker-container.py;
5. Run start-kafka-broker-1.py
6. Run start-kafka-broker-2.py
7. Run create_persons-info_topic.py
8. Run start-application.py

REST Api

GET      /person/all           - get all persons data in database
POST     /person               - send massage to broker with random person data
DELETE   /person/all           - remove all persons data
GET      /person/offset/all    - get offset for each partition in topic (only for 'exatly once' strategy) 
DELETE   /person/offset/all    - remove all offsets
