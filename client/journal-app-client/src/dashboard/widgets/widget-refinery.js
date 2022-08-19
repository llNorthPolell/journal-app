export function processDashboardWidgets(config, journalEntriesList) {
    if (config==null){
        console.log("Error: config is undefined.");
        return;
    } 

    function generateLastEntry(){
        let payload = {
            overview: "You have no journal entries at the moment. Please create one."
        };
        if (journalEntriesList==null || journalEntriesList.length===0) return payload;

        const lastEntry = journalEntriesList[journalEntriesList.length-1];
        payload = {
            overview: lastEntry.overview,
            key: lastEntry.key
        }
        return payload;
    }

    function generateLineGraph(widget){
        let payload = {
            title : widget.title,
            labels : widget.labels,
            data : {
                x: [],
                y: [...widget.data.yValues]
            }
        };
        payload.data.y.forEach(y=>
            y.data=[]
        );

/*
let xList=[
	{a:1,b:[{c:1,d:[{e:1,f:2}]},{c:2,d:[{e:2,f:4}]}]},
    {a:2,b:[{c:1,d:[{e:1,f:2},{e:2,f:4}]}]},
    {a:3,b:[{c:1,d:[{e:1},{e:2,f:4}]},{c:2,d:[{e:3,f:6},{e:4,f:8}]}]}
]

let eList = [1,2,3]

let dList = xList.map(x=>
	({
    	a: x.a,
        f: 	x.b.map(b=>
              b.d.filter(d=>eList.includes(d.e)).map(d=>
                 d.f
              )
            ).flat()
    })


);

let output = dList.map(d=>({a:d.a, f:d.f}))



console.log(JSON.stringify(output));
*/
        const yValues = widget.data.yValues.map(y=>
                            ({
                                topic:y.topic,
                                record:y.record
                            })
                        );

        const data = journalEntriesList.map(journalEntry=>
                        ({
                        x:journalEntry[widget.data.xValue],
                        y:journalEntry.journalBodyItems.map(journalBodyItem=> 
                                journalBodyItem.recordList.filter(record=> 
                                    yValues.some(y=>y.topic===journalBodyItem.topic && y.record===record.key)
                                ).map(record=>
                                    ({topic: journalBodyItem.topic, record: record.key, value: record.value})
                                )
                            ).flat()
                        })    
                    );
        console.log("yValues : "+ JSON.stringify(yValues));
        console.log("Data : "+ JSON.stringify(data));

        let i = 1;
        data.forEach(data=>{
            payload.data.x.push(data.x);

            payload.data.y.forEach(payloadY=>{
                data.y.forEach(dataY=>{
                    if (payloadY.topic===dataY.topic && payloadY.record===dataY.record)
                        payloadY.data.push(dataY.value)
                })

                if (payloadY.data.length < i) payloadY.data.push(0);
            });

            i++;
        })        


        return payload;
    }

    let output=[];
    config.forEach(
        widget=> {
            const processedWidget={...widget};
            switch(widget.type){
                case "last-entry":
                    processedWidget.payload = generateLastEntry();
                    break;
                case "line-graph":
                    processedWidget.payload = generateLineGraph(widget);
                    break;
                default: 
                    break;
            }

            if (processedWidget.payload)
                output.push(processedWidget)
        }
    )

    return output;
}


