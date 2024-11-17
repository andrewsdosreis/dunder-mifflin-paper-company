import json

def lambda_handler(event, context):
    character_name = event.get('queryStringParameters', {}).get('name', '').lower()

    # Load the trivia data from the JSON file
    with open('trivia-data.json', 'r') as f:
        trivia_data = json.load(f)

    # Find the trivia for the given character
    trivia = trivia_data.get(character_name, "Character not found.")

    # Prepare the response
    response = {
        'statusCode': 200,
        'body': json.dumps({'trivia': trivia})
    }

    return response

# if __name__ == '__main__':
#     # Simulate an event with query parameters
#     event = {
#         'queryStringParameters': {
#             'name': 'Michael Scott'
#         }
#     }
#     context = None  # You can leave context as None for testing purposes
#
#     # Call the lambda_handler function with the simulated event
#     response = lambda_handler(event, context)
#
#     # Print the response
#     print(response)