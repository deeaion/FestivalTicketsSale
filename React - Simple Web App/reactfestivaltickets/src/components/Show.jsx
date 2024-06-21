// Show.jsx
// eslint-disable-next-line react/prop-types
const Show = ({ show, onEdit, onDelete, onAdd }) => {
    return (
        <div>
            <span>{show.locatie}</span>
            <span>{show.data.split('T')[0]}</span>
            <span>{show.nrLocuriDisponibile}</span>
            <span>{show.nrLocuriVandute}</span>
            <span>{show.artist}</span>
            <button onClick={() => onEdit(show)}>Edit</button>
            <button onClick={() => onDelete(show.id)}>Delete</button>
            <button onClick={() => onAdd(show)}>Add</button> {/* Pass the show object */}
        </div>
    );
};

export default Show;
