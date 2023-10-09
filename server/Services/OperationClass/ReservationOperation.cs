//CRUD operations that can be performed on reservations model

using System.Threading.Tasks;
using web_service.Models;
using web_service.Helper;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using MongoDB.Bson;



namespace web_service.Operation
{
    
    public class ReservationOperation

    {
        private readonly IMongoCollection<ReservationModel> _reservationCollection;
        private readonly IMongoCollection<BsonDocument> _genericBSONDoc;

        //Constructor for the ReservationOperation class
        public ReservationOperation(IMongoCollection<ReservationModel> reservationCollection, IMongoCollection<BsonDocument> genericBSONDoc)
        {
            _reservationCollection = reservationCollection;
            _genericBSONDoc = genericBSONDoc;
        }
        //Insert a reservation
        public async Task CreateAsync(ReservationModel reservationModel)
        {
            await _reservationCollection.InsertOneAsync(reservationModel);
        }
        //Find a reservation by its ID 
        public async Task<ReservationModel> FindByIdAsync(string id)
        {
            var filter = Builders<ReservationModel>.Filter.Eq("_id", id);
            return await _reservationCollection.Find(filter).FirstOrDefaultAsync();
        }
        //Find a reservation by its ID and also getting data from user and trainschedule collections
        public async Task<BsonDocument> FindByIdAndExpandAsync(string id)
        {

            ObjectId objectId = new ObjectId(id);

            //aggregation to find the related data based on the IDs
            BsonDocument pipelineStage1 = new BsonDocument{
                {
                    "$match", new BsonDocument{
                        { "_id", objectId },
                    }
                }
            };

            //shaping the output documents
            // _id, status, train, date should be included in the final output.

            BsonDocument pipelineStage2 = new BsonDocument{
                    {
                        "$project", new BsonDocument{
                            { "_id", 1 },
                            { "status", 1 },
                            { "train", 1},
                            { "date", 1 },

                        }
                    }
                };

            // The $lookup is used to matches documents based on the "train" 
            //field in the current collection and the "_id" field in the "trainShedule" collection.

            BsonDocument pipelineStage31 = new BsonDocument{
                {
                    "$lookup", new BsonDocument{
                        { "from", "trainShedule" },
                        { "localField", "train" },
                        { "foreignField", "_id" },
                        { "as", "train" }
                    }
                }
            };
            
            //Match documents based on the 'userID' field in the reservations collection and the "_id" field in the "travellers" collection.
            BsonDocument pipelineStage32 = new BsonDocument{
                {
                    "$lookup", new BsonDocument{
                        { "from", "traveller" },
                        { "localField", "userID" },
                        { "foreignField", "_id" },
                        { "as", "user" }
                    }
                }
            };
            //Match documents based on the 'travelAget' field in the reservations collection and the "_id" field in the "admin" collection.
            BsonDocument pipelineStage33 = new BsonDocument{
                {
                    "$lookup", new BsonDocument{
                        { "from", "admin" },
                        { "localField", "travelAgent" },
                        { "foreignField", "_id" },
                        { "as", "agent" }
                    }
                }
            };


            // The following fields are excluded (set to 0) from the output.

            BsonDocument pipelineStage4 = new BsonDocument{
                {
                    "$project", new BsonDocument{

                        { "agent.password", 0 },
                        { "user.password", 0 },
                        { "user.active", 0 },
                        { "user._isDeleted", 0 },
                        { "agent.active", 0 },
                        { "agent._isDeleted", 0 }
                    }
                }
            };

            //aggregation of pipelines
            BsonDocument[] pipeline = new BsonDocument[] {
                pipelineStage1,
                // pipelineStage2,
                pipelineStage32,
                pipelineStage33,
                pipelineStage31,
                pipelineStage4
            };
            var users = await _genericBSONDoc.Aggregate<BsonDocument>(pipeline).FirstAsync();

            return users;
        }

        public async Task<List<ReservationModel>> FindByTrainIdAsync(string r_id)
        {
            var filter = Builders<ReservationModel>.Filter.Eq("train", r_id);
            return await _reservationCollection.Find(filter).ToListAsync();
        }


        public async Task<ReservationModel> UpdateAsync(string id, ReservationBase reservationModel)
        {

            //updateList store a list of update operations to be applied to ReservationModel

            var updateList = new List<UpdateDefinition<ReservationModel>>();

            updateList.Add(reservationModel.userID != null ? Builders<ReservationModel>.Update.Set(rec => rec.userID, reservationModel.userID) : null);
            updateList.Add(reservationModel.train != null ? Builders<ReservationModel>.Update.Set(rec => rec.train, reservationModel.train) : null);
            updateList.Add(reservationModel.travelAgent != null ? Builders<ReservationModel>.Update.Set(rec => rec.travelAgent, reservationModel.travelAgent) : null);
            updateList.Add(reservationModel.date != null ? Builders<ReservationModel>.Update.Set(rec => rec.date, reservationModel.date) : null);
            updateList.Add(reservationModel.agentNote != null ? Builders<ReservationModel>.Update.Set(rec => rec.agentNote, reservationModel.agentNote) : null);
            updateList.Add(reservationModel.status != null ? Builders<ReservationModel>.Update.Set(rec => rec.status, reservationModel.status) : null);
            updateList.Add(reservationModel.trainClass != null ? Builders<ReservationModel>.Update.Set(rec => rec.trainClass, reservationModel.trainClass) : null);


            updateList.RemoveAll(update => update == null);

            var _update = Builders<ReservationModel>.Update.Combine(updateList);


            var filter = Builders<ReservationModel>.Filter.Eq("_id", id);

            var options = new FindOneAndUpdateOptions<ReservationModel>
            {
                ReturnDocument = ReturnDocument.After,
            };


            var updatedDocument = await _reservationCollection.FindOneAndUpdateAsync(
                filter,
               _update,
                options
            );

            return updatedDocument;
        }


        public async Task<bool> DeleteAsync(string id)
        {
            var filter = Builders<ReservationModel>.Filter.Eq("_id", id);
            var update = Builders<ReservationModel>.Update
                .Set("_isDeleted", true)
                .Set("active", false)
                .Set("published", false);

            var result = await _reservationCollection.UpdateOneAsync(filter, update);
            return result.ModifiedCount > 0;
        }

        public async Task<List<BsonDocument>> GetByStatusAgentAsync(string status, string _id)
        {
            ObjectId objectId = new ObjectId(_id);


            BsonDocument pipelineStage1 = new BsonDocument{
                {
                    "$match", new BsonDocument{
                        { "_isDeleted", false },
                        { "travelAgent", objectId },
                        { "status", status },

                    }
                }
            };


            BsonDocument pipelineStage2 = new BsonDocument{
                    {
                        "$project", new BsonDocument{
                            { "_id", 1 },
                            { "status", 1 },
                            { "train", 1},
                            { "date", 1 },

                        }
                    }
                };

            BsonDocument pipelineStage3 = new BsonDocument{
                {
                    "$lookup", new BsonDocument{
                        { "from", "trainShedule" },
                        { "localField", "train" },
                        { "foreignField", "_id" },
                        { "as", "train" }
                    }
                }
            };
            BsonDocument pipelineStage4 = new BsonDocument{
                {
                    "$project", new BsonDocument{
                        { "_id", 1 },
                        { "status", 1 },
                        { "date", 1 },
                        { "train.to", 1 },
                        { "train.from", 1 }
                    }
                }
            };


            BsonDocument[] pipeline = new BsonDocument[] {
                pipelineStage1,
                pipelineStage2,
                pipelineStage3,
                pipelineStage4
            };
            var users = await _genericBSONDoc.Aggregate<BsonDocument>(pipeline).ToListAsync(); ;

            return users;
        }

        //get documnets based on the status of the reservation
        public async Task<List<BsonDocument>> GetByStatusTravellerAsync(string status, string _id)
        {

            ObjectId objectId = new ObjectId(_id);

            //match documnets with specified fields
            BsonDocument pipelineStage1 = new BsonDocument{
                {
                    "$match", new BsonDocument{
                        { "_isDeleted", false },
                        { "userID", objectId },
                        { "status", status },

                    }
                }
            };


            BsonDocument pipelineStage2 = new BsonDocument{
                    {
                        "$project", new BsonDocument{
                            { "_id", 1 },
                            { "status", 1 },
                            { "train", 1},
                            { "date", 1 },

                        }
                    }
                };
            //lookup for the train object based on the train id
            BsonDocument pipelineStage3 = new BsonDocument{
                {
                    "$lookup", new BsonDocument{
                        { "from", "trainShedule" },
                        { "localField", "train" },
                        { "foreignField", "_id" },
                        { "as", "train" }
                    }
                }
            };

            //including the fields that should be in the resulting output
            BsonDocument pipelineStage4 = new BsonDocument{
                {
                    "$project", new BsonDocument{
                        { "_id", 1 },
                        { "status", 1 },
                        { "date", 1 },
                        { "train.to", 1 },
                        { "train.from", 1 }
                    }
                }
            };


            BsonDocument[] pipeline = new BsonDocument[] {
                pipelineStage1,
                pipelineStage2,
                pipelineStage3,
                pipelineStage4
            };
            var users = await _genericBSONDoc.Aggregate<BsonDocument>(pipeline).ToListAsync(); ;

            return users;
        }

        //return users with where he documents is_deleted is equal to false.
        public async Task<List<ReservationModel>> GetAllAsync()
        {
            var filter = Builders<ReservationModel>.Filter.And(
                Builders<ReservationModel>.Filter.Eq("_isDeleted", false)
            );

            var users = await _reservationCollection.Find(filter).ToListAsync();

            return users;
        }

    }
}