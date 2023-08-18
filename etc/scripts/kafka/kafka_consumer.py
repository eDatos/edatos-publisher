from confluent_kafka.avro import AvroConsumer

def consume_record(bootstrap_servers, schema_registry, topic):
    default_group_name = "default-consumer5-group"

    consumer_config = {"bootstrap.servers": bootstrap_servers,
                       "schema.registry.url": schema_registry,
                       "group.id": default_group_name,
                       "auto.offset.reset": "earliest"}

    consumer = AvroConsumer(consumer_config)

    consumer.subscribe([topic])

    try:
        message = consumer.poll()
    except Exception as e:
        print(f"Exception while trying to poll messages - {e}")
    else:
        if message:
            print(f"Successfully poll a record from "
                  f"Kafka topic: {message.topic()}, partition: {message.partition()}, offset: {message.offset()}\n"
                  f"message key: {message.key()} || message value: {message.value()}")
            consumer.commit()
        else:
            print("No new messages at this point. Try again later.")

    consumer.close()


if __name__ == "__main__":
    consume_record("localhost:9092", "http://localhost:8081", "OPERATION_PUBLICATIONS")
