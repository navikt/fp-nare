'use strict';

// ************** Generate the tree diagram	 *****************
var margin = {top: 20, right: 120, bottom: 20, left: 120},
    width = 2000 - margin.right - margin.left,
    height = 1000 - margin.top - margin.bottom;

var i = 0;

var tree = d3.layout.tree()
    .size([height, width]);

var diagonal = d3.svg.diagonal()
    .projection(function (d) {
        return [d.y, d.x];
    });

var svg = d3.select("body").append("svg")
    .style("background", "#212121")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

// load the external data
d3.json("../../output/modrekvote.json", function (error, root) {
    update(root);
});

var tip = d3.tip()
    .attr('class', 'd3-tip')
    .offset([-10, 0])
    .html(function(d) {
        return "<div> ID: <span style='color:lightblue'>"+ d.ruleIdentifcation+"</span></div> " +
            "<div> Beskrivelse: <div style='color:lightblue'>"+ d.ruleDescription+"</span></div> " +
            "<div> Evaluering: <div style='color:lightblue'>"+ d.reason+"</div></div>";
    })

svg.call(tip);

function update(source) {

    // Compute the new tree layout.
    var nodes = tree.nodes(source).reverse(),
        links = tree.links(nodes);

    // Normalize for fixed-depth.
    nodes.forEach(function (d) {
        d.y = d.depth * 180;
    });

    // Declare the nodes…
    var node = svg.selectAll("g.node")
        .data(nodes, function (d) {
            return d.id || (d.id = ++i);
        });

    // Enter the nodes.
    var nodeEnter = node.enter().append("g")
        .attr("class", "node")
        .attr("transform", function (d) {
            return "translate(" + d.y + "," + d.x + ")";
        });


    function getType(d) {
        switch (d.operator) {
            case "AND" :
                return "square";
            case "OR" :
                return "diamond";
            default:
                return "circle";
        }
    }

    nodeEnter.append("path")
        .attr("d", d3.svg.symbol()
            .size( function(d) { return 20*20 })
            .type( function(d) { return getType(d) })
        )
        .attr("transform", "translate( 50, 0 )")
        .style("fill", "#316470")
        .style("stroke","#BEBEBE")
        .style("stroke-width",3)
        .style("opacity",1);

    nodeEnter.append("rect")
        .attr("class", "boks")
        .attr("width", 100)
        .attr("height", 60)
        .attr("y", -30)
        .attr("x", -50)
        .attr("rx", 10)
        .attr("ry",10)
        .style("fill", "#20AFB1")
        .style("stroke","#BEBEBE")
        .style("stroke-width",3)
        .style("opacity",1);

    nodeEnter.append("text")
        .attr("class", "teksten")
        .attr("text-anchor", "middle")
        .text(function (d) {
            return d.ruleDescription;
        })

        .style("fill", "#212121");





    // Declare the links…
    var link = svg.selectAll("path.link")
        .data(links, function (d) {
            return d.target.id;
        });

    // Enter the links.
    link.enter().insert("path", "g")
        .attr("class", "link")
        .attr("d", diagonal);



    function wrap(text, width) {
        text.each(function() {
            var text = d3.select(this),
                words = text.text().split(/\s+/).reverse(),
                word,
                line = [],
                lineNumber = 0,
                lineHeight = 1.1, // ems
                y = 0,
                dy = -0.5,
                tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");

            while (word = words.pop()) {
                line.push(word);
                tspan.text(line.join(" "));
                if (tspan.node().getComputedTextLength() > width) {
                    line.pop();
                    tspan.text(line.join(" "));
                    line = [word];
                    tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").text(word);
                }
            }
        });
    }

    node.selectAll("text").call(wrap, 90);

    var defaults = {
        "width": 100,
        "height": 100,
        "resize": false
    }

    //// Wrap text in a rectangle, and size the text to fit.
    //d3plus.textwrap()
    //    .dev(true)
    //    .config(defaults)
    //    .container(d3.selectAll("text.teksten"))
    //    .draw();

}