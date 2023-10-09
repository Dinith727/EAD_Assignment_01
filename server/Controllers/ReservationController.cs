
using Microsoft.AspNetCore.Mvc;
using System;
using web_service.Services;
using web_service.Models;
using web_service.Utility;
using web_service.Helper;
using web_service.Formatter;
using Microsoft.AspNetCore.Authorization;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;
using MongoDB.Bson;
using System.Collections.Immutable;
using System.Text.Json.Nodes;
using Newtonsoft.Json;
using MongoDB.Bson.IO;



namespace web_service.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ReservationController : ControllerBase
{

    private readonly MongoDBService _mongoDBService;

    public ReservationController(MongoDBService mongoDBService)
    {
        _mongoDBService = mongoDBService;
    }

    [Authorize(Roles = "backOffice,travelAgent,traveller")]
    [HttpGet]
    async public Task<IActionResult> Get([FromQuery] string? id)
    {
        try
        {
            // Attempt to find a reservation by its 'id' 
            var val = await _mongoDBService.Reservation.FindByIdAndExpandAsync(id ?? "");

            if (val == null)
            {
                return NotFound(new ErrorResponse
                {
                    ErrorCode = 404,
                    Message = "Not Found",
                    Details = "Requested reservation not Found"
                });
            }


            var jsonContent = val.ToJson();

            var contentResult = new ContentResult
            {
                Content = jsonContent,
                ContentType = "application/json", // Set the content type to JSON
                StatusCode = 200 // Status code for OK
            };

            return contentResult;

        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }


    [Authorize(Roles = "travelAgent,traveller")]
    [HttpGet("all")]
    async public Task<IActionResult> GetAll([FromQuery] string type)
    {
        try
        {
            List<BsonDocument> val = new List<BsonDocument>();

            var claims = HttpContext.User.Claims;
            if (claims.IsNullOrEmpty())
            {
                return Unauthorized(new ErrorResponse
                {
                    ErrorCode = 401,
                    Message = "Invalid token",
                    Details = "No relevent user claims found"
                });
            }

            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            var role = claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);

            if (role.Value == "travelAgent")
            {
                val = await _mongoDBService.Reservation.GetByStatusAgentAsync(type, _id.Value ?? "");
            }
            else
            {
                val = await _mongoDBService.Reservation.GetByStatusTravellerAsync(type, _id.Value ?? "");

            }

            var jsonContent = val.ToJson();

            var contentResult = new ContentResult
            {
                Content = jsonContent,
                ContentType = "application/json", // Set the content type to JSON
                StatusCode = 200 // Status code for OK
            };

            return contentResult;


        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }


    [Authorize(Roles = "travelAgent,traveller")]
    [HttpPost("add")]
    async public Task<IActionResult> Add([FromBody] ReservationModel reservation)
    {

        try
        {

            var claims = HttpContext.User.Claims;
            if (claims.IsNullOrEmpty())
            {
                return Unauthorized(new ErrorResponse
                {
                    ErrorCode = 401,
                    Message = "Invalid token",
                    Details = "No relevent user claims found"
                });
            }

            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            var role = claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);

            if (role.Value == "travelAgent")
            {
                reservation.travelAgent = _id.Value ?? "";
            }
            else
            {
                reservation.userID = _id.Value ?? "";

            }

            reservation.status = "draft";
            reservation._isDeleted = false;
            await _mongoDBService.Reservation.CreateAsync(reservation);

            return Ok(new DataResponse<ReservationModel>
            {
                status = 200,
                data = reservation
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

    [Authorize(Roles = "travelAgent,traveller")]
    [HttpPost("update")]
    async public Task<IActionResult> Update([FromBody] ReservationBase _reservation)
    {
        try
        {


            var claims = HttpContext.User.Claims;
            if (claims.IsNullOrEmpty())
            {
                return Unauthorized(new ErrorResponse
                {
                    ErrorCode = 401,
                    Message = "Invalid token",
                    Details = "No relevent user claims found"
                });
            }

            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            var role = claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);

            if (role.Value == "travelAgent")
            {
                _reservation.travelAgent = _id.Value ?? "";
            }
            else
            {
                _reservation.userID = _id.Value ?? "";

            }

            var _reservationModel = await _mongoDBService.Reservation.FindByIdAsync(_reservation._id ?? "");

            DateTime currentDate = DateTime.Now;

            DateTime futureDate = currentDate.AddDays(5);

            if (_reservationModel.date < futureDate && _reservationModel.status == "upcoming")
            {
                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Cannot Perform Update",
                    Details = " Unable to cancel reservation within 5 days of departure."
                });
            }

            var _res = await _mongoDBService.Reservation.UpdateAsync(_reservation._id ?? "", _reservation);

            if (_res == null)
            {

                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Update Unsuccessfull",
                    Details = "The reservation detail update unsucessful"
                });
            }
            else
            {
                return Ok(new DataResponse<ReservationModel>
                {
                    status = 200,
                    data = _res
                });

            }


        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    [Authorize(Roles = "travelAgent,traveller")]
    [HttpDelete("delete")]
    // public IActionResult Update([FromBody] AdminModel model)
    async public Task<IActionResult> Delete([FromQuery] string id)
    {
        try
        {

            var _reservationModel = await _mongoDBService.Reservation.FindByIdAsync(id ?? "");

            DateTime currentDate = DateTime.Now;

            DateTime futureDate = currentDate.AddDays(5);

            if (_reservationModel.date < futureDate)
            {
                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Cannot Perform Delete",
                    Details = " Unable to cancel reservation within 5 days of departure."
                });
            }

            await _mongoDBService.Reservation.DeleteAsync(id ?? "");
            return Ok(new DataResponse<string>
            {
                status = 200,
                data = "Succesfully Deleted"
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

}
