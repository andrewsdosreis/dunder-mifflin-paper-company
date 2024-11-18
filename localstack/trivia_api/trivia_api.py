import json

def lambda_handler(event, context):
    character_name = event.get('queryStringParameters', {}).get('name', '')

    try:
        with open('trivia-data.json', 'r') as f:
            trivia_data = json.load(f)
    except FileNotFoundError:
        return {
            'statusCode': 500,
            'body': json.dumps({'error': 'Trivia data file not found.'})
        }

    trivia = trivia_data.get(character_name, "Character not found.")

    # Check if the character exists in the trivia data
    if character_name in trivia_data:
        trivia = trivia_data[character_name]
        return {
            'statusCode': 200,
            'body': json.dumps({'trivia': trivia})
        }
    else:
        # Return 404 when character is not found
        return {
            'statusCode': 404,
            'body': json.dumps({'error': 'Character not found.'})
        }

# if __name__ == '__main__':
#     # Simulate an event with query parameters
#     event = {
#         'queryStringParameters': {
#             'name': 'Michael_Scott'
#         }
#     }
#     context = None  # You can leave context as None for testing purposes
#
#     # Call the lambda_handler function with the simulated event
#     response = lambda_handler(event, context)
#
#     # Print the response
#     print(response)