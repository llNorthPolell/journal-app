const listUtil = (list,setList,action) => {
    switch(action.type){
        case "INSERT":
            setList([...list, action.payload]);
            break;
        case "GET":
            return list.find(item=>{return item.id===action.id});
        case "UPDATE":
            if (action.id) 
                setList(list.map(item=>
                    (item.id === action.id)?action.payload : item  
                ));
            else if (action.key)
                setList(list.map(item=>
                    (item.key === action.key)?action.payload : item  
                ));         
            break;
        case "DELETE":
            if (action.id)
                setList(list.filter(item => item.id!==action.id));
            else if (action.key)
                setList(list.filter(item => item.key!==action.key));
            else
                setList(list.filter(item => item!==action.payload));
            break;
        case "CONTAINS":
            return list.some(item=> item == action.payload);
        case "SET":
            setList([...action.payload]);
            break;
        case "TRUNCATE":
            setList([]);
            break;
        default:
            return list;
    }
}
export default listUtil;