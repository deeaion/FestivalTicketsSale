const ShowInfo = ({event}) => {
    return (
        <div className="show-information">
            <h1>Show Information</h1>
            <p>Location: {event.locatie}</p>
            <p>Date: {event["data"]}</p>
            <p>Available Tickets: {event.nrLocuriDisponibile}</p>
            <p>Sold Tickets: {event.nrLocuriVandute}</p>
            <p>Artist: {event.artist}</p>

        </div>
    )
}
export default ShowInfo;