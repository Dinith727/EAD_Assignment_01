//CRUD operations for the traveller model

using System.Threading.Tasks;
using web_service.Models;
using web_service.Helper;
using Microsoft.Extensions.Options;
using MongoDB.Driver;


namespace web_service.Operation
{

    public class TravellerOperation

    {
        private readonly IMongoCollection<TravellerModel> _travellerCollection;

        //Constructor
        public TravellerOperation(IMongoCollection<TravellerModel> travellerCollection)
        {
            _travellerCollection = travellerCollection;
        }

        // Insert a new TravellerModel document
        public async Task CreateAsync(TravellerModel travellerModel)
        {
            await _travellerCollection.InsertOneAsync(travellerModel);
        }

        // Find a TravellerModel document by its ID
        public async Task<TravellerModel> FindByIdAsync(string id)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            return await _travellerCollection.Find(filter).FirstOrDefaultAsync();
        }

        // Find a TravellerModel document by email
        public async Task<TravellerModel> FindByEmailAsync(string email)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("email", email);
            return await _travellerCollection.Find(filter).FirstOrDefaultAsync();
        }

        // Update a TravellerModel document by its ID
        public async Task<TravellerModel> UpdateAsync(string id, TravellerModel traveller)
        {
            // Build a list of update definitions based on the provided TravellerModel
            var updateList = new List<UpdateDefinition<TravellerModel>>();

            updateList.Add(traveller.fullName != null ? Builders<TravellerModel>.Update.Set(rec => rec.fullName, traveller.fullName) : null);
            updateList.Add(traveller.email != null ? Builders<TravellerModel>.Update.Set(rec => rec.email, traveller.email) : null);
            updateList.Add(traveller.NIC != null ? Builders<TravellerModel>.Update.Set(rec => rec.NIC, traveller.NIC) : null);

            // Remove null updates and combine the remaining updates
            updateList.RemoveAll(update => update == null);

            var _update = Builders<TravellerModel>.Update.Combine(updateList);

            // Find and update the document
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TravellerModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _travellerCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }

        // Update the "active" status of a TravellerModel document by its ID
        public async Task<TravellerModel> UpdateActiveAsync(string id, bool _active)
        {

            var _update = Builders<TravellerModel>.Update.Combine(

             Builders<TravellerModel>.Update.Set((rec => rec.active), _active)
            );

            // Find and update the document
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TravellerModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _travellerCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }

        // Update the password of a TravellerModel document by its ID
        public async Task<TravellerModel> UpdatePasswordAsync(string id, string password)
        {

            var _update = Builders<TravellerModel>.Update.Combine(

             Builders<TravellerModel>.Update.Set((rec => rec.password), password)
            );

            // Find and update the document
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var options = new FindOneAndUpdateOptions<TravellerModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _travellerCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }

        // Soft delete a TravellerModel document by setting "_isDeleted" to true and deactivating the user
        public async Task<bool> DeleteAsync(string id)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var update = Builders<TravellerModel>.Update
                .Set("_isDeleted", true)
                .Set("email", id)
                .Set("active", false);

            var result = await _travellerCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }
        // Deactivate a TravellerModel document by setting "active" to false
        public async Task<bool> DeActivateAsync(string id)
        {
            var filter = Builders<TravellerModel>.Filter.Eq("_id", id);
            var update = Builders<TravellerModel>.Update
                .Set("active", false);

            var result = await _travellerCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }
        // Get a list of active TravellerModel documents (not deleted and active)
        public async Task<List<TravellerModel>> GetActiveAsync()
        {
            var filter = Builders<TravellerModel>.Filter.And(
                Builders<TravellerModel>.Filter.Eq("_isDeleted", false),
                Builders<TravellerModel>.Filter.Eq("active", true)
            );

            var users = await _travellerCollection.Find(filter).ToListAsync();

            return users;
        }
        // Get a list of all TravellerModel documents (not deleted)
        public async Task<List<TravellerModel>> GetAllAsync()
        {
            var filter = Builders<TravellerModel>.Filter.And(
                Builders<TravellerModel>.Filter.Eq("_isDeleted", false)
            );

            var users = await _travellerCollection.Find(filter).ToListAsync();

            return users;
        }

    }
}