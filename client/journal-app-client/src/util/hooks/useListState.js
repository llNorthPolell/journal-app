import {useState} from 'react'



const useListState = initList => {
    const [list, setList] = useState(initList);

    const insert = newItem => {
        setList([...list, newItem]);
    }

    const remove = (id, deleteItem) => {
        const updatedList = (id !== undefined)?
            list.filter(item => item.id!==id):
            list.filter(item => item!==deleteItem);

        setList(updatedList);
    }

    const update = (newItem) =>{
        const updatedList = list.map(item=>
            item.id === newItem.id ? newItem : item   
        );
        setList(updatedList);
    }

    const overrideList = newList => {
        setList(newList);
    }

    const contains = checkItem => {
        return list.some(item=> item == checkItem);
    }
    
    return [list, overrideList, insert, update, remove, contains];
}

export default useListState;
