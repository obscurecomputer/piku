local ITERATIONS = 1_000_000 -- How many times to run each test
local WARM_UP_COUNT = 10_000 -- Initial runs to "warm up" the VM

local function benchmark(name: string, testFunction: () -> ())
    -- 1. Warm-up (Ensures Luau compiler optimizes the hot path)
    for _ = 1, WARM_UP_COUNT do
        testFunction()
    end

    -- 2. Actual Timing
    local startTime = os.clock()
    
    for _ = 1, ITERATIONS do
        testFunction()
    end
    
    local endTime = os.clock()
    local totalTime = endTime - startTime
    local averageTime = totalTime / ITERATIONS

    client.send(string.format("--- %s ---", name))
    client.send(string.format("Total Time:   %.6f s", totalTime))
    client.send(string.format("Avg per Call: %.9f s", averageTime))
    client.send("")
end

---------------------------------------------------------
-- TEST CASES
---------------------------------------------------------

-- Example 1: Standard Table Insertion
local function testInsert()
    local t = {}
    for i = 1, 100 do
        table.insert(t, i)
    end
end

-- Example 2: Table Pre-allocation (Optimized Luau way)
local function testPreAllocated()
    local t = table.create(100)
    for i = 1, 100 do
        t[i] = i
    end
end

-- Run the tests
client.send(string.format("Running benchmarks with %d iterations...\n", ITERATIONS))

benchmark("Standard Insert", testInsert)
benchmark("Pre-allocated Insert", testPreAllocated)