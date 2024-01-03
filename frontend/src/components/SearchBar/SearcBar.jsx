import React, {useState} from 'react'


const searchBar = () => {

    const [searchInput, setSearchInput] = useState("");

    const stanze = [

        { name: "", id:""},
    ];

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    if (searchInput.length > 0) {
        stanze.filter((stanze) => {
            return stanze.name.match(searchInput);
        });
    }

    return <div>

        <input
            type="search"
            placeholder="Search here"
            onChange={handleChange}
            value={searchInput} />

        <table>
            <tr>
                <th>name</th>
                <th>id</th>
            </tr>

            {stanze.map((stanze, *index*) => {

                <div>
                <tr>
                <td>{stanze.name}</td>
        <td>{country.continent}</td>
    </tr>
</div>

})}
</table>

</div>


};

export default searchBar;