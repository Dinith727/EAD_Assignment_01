namespace web_service.Models
{

// Reference : https://www.mongodb.com/developer/languages/csharp/create-restful-api-dotnet-core-mongodb/
    public class MongoDBSettings
    {

        public string ConnectionURI { get; set; } = null!;
        public string DatabaseName { get; set; } = null!;
        public string CollectionName { get; set; } = null!;

    }
}