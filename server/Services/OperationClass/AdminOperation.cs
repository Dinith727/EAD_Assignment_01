
//CRUD operations that can be performed on admin model

using System.Threading.Tasks;
using web_service.Models;
using web_service.Helper;
using Microsoft.Extensions.Options;
using MongoDB.Driver;


namespace web_service.Operation
{

    public class AdminOperations

    {
        private readonly IMongoCollection<AdminModel> _adminCollection;

        public AdminOperations(IMongoCollection<AdminModel> adminCollection)
        {
            _adminCollection = adminCollection;
        }
        //admin create operations
        public async Task CreateAsync(AdminModel adminModel)
        {
            await _adminCollection.InsertOneAsync(adminModel);
        }

        public async Task<AdminModel> FindByIdAsync(string id)
        {
            var filter = Builders<AdminModel>.Filter.Eq("_id", id);
            return await _adminCollection.Find(filter).FirstOrDefaultAsync();
        }
        public async Task<AdminModel> FindByUsernameAsync(string username)
        {
            var filter = Builders<AdminModel>.Filter.Eq("username", username);
            return await _adminCollection.Find(filter).FirstOrDefaultAsync();
        }
        //admin update operations
        public async Task<AdminModel> UpdateAsync(string id, AdminModel admin)


        {
            var updateList = new List<UpdateDefinition<AdminModel>>();

            updateList.Add(admin.username != null ? Builders<AdminModel>.Update.Set(rec => rec.username, admin.username) : null);
            updateList.Add(admin.role != null ? Builders<AdminModel>.Update.Set(rec => rec.role, admin.role) : null);


            updateList.RemoveAll(update => update == null);


            var _update = Builders<AdminModel>.Update.Combine(

            updateList
            );



            var filter = Builders<AdminModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<AdminModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            // System.Console.WriteLine();

            var updatedDocument = await _adminCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }
        //admin Delete operations
        public async Task<bool> DeleteAsync(string id)
        {
            var filter = Builders<AdminModel>.Filter.Eq("_id", id);
            var update = Builders<AdminModel>.Update
                .Set("_isDeleted", true)
                .Set("username", id)
                .Set("active", false);

            var result = await _adminCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }
        //activate admin users operations
        public async Task<bool> ActivateAsync(string id, bool active)
        {
            var filter = Builders<AdminModel>.Filter.Eq("_id", id);
            var update = Builders<AdminModel>.Update
                .Set("active", active);

            var result = await _adminCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }

        public async Task<List<AdminModel>> GetActiveAsync()
        {
            var filter = Builders<AdminModel>.Filter.And(
                Builders<AdminModel>.Filter.Eq("_isDeleted", false),
                Builders<AdminModel>.Filter.Eq("active", true)
            );

            var users = await _adminCollection.Find(filter).ToListAsync();

            return users;
        }
        //get travel agents
        public async Task<List<AdminModel>> getAgents()
        {
            var filter = Builders<AdminModel>.Filter.And(
                Builders<AdminModel>.Filter.Eq("_isDeleted", false),
                Builders<AdminModel>.Filter.Eq("active", true),
                Builders<AdminModel>.Filter.Eq("role", "travelAgent")
            );

            var users = await _adminCollection.Find(filter).ToListAsync();

            return users;
        }
        // get all admin operations
        public async Task<List<AdminModel>> GetAllAsync()
        {
            var filter = Builders<AdminModel>.Filter.And(
                Builders<AdminModel>.Filter.Eq("_isDeleted", false)
            );

            var users = await _adminCollection.Find(filter).ToListAsync();

            return users;
        }

    }
}