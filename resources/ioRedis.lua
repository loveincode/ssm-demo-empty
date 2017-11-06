local rdebug =     redis.LOG_DEBUG
local rverb =     redis.LOG_VERBOSE
local rntf =    redis.LOG_NOTICE
local rwarn =     redis.LOG_WARNING

local rlog = function(m)
  redis.log(rntf,m)
end
-- 待完成， admin的处理，  搜索，分页查询
local function printTab(tab)
  for i,v in pairs(tab) do
    if type(v) == "table" then
      rlog("table " .. i .. "{")
      printTab(v)
      rlog("}")
    else
      rlog(i .. " -- " .. tostring(v))
    end
  end
end


-- rlog("KEYS:".. #KEYS );
for i=1,#KEYS
do
  -- rlog(KEYS[i])
end

-- rlog("ARGV:" .. #ARGV)
local argvsg = ''
for i=1,#ARGV
do
  -- rlog(type(ARGV[i]))
  -- rlog(ARGV[i])
end

--  function
local debug_flag = true
local function debug(msg,t,ZorS)
  if debug_flag then
    ZorS = ZorS or "Z"
    -- rlog("DEBUG]] " ..msg)
    if type(t) == "table" then
      t = t or {}
      printTab(t)
    elseif  type(t) == "string" then
      if ZorS == "Z" then
        t = redis.call("ZRANGE",t,0,-1)
      elseif ZorS == "S" then
        t = redis.call("SMEMBERS",t)
      end
      printTab(t)
    end
    -- rlog("\n")
  end
end

local function unionRedisSet(ta,dst,step)
  redis.call("DEL",dst )
  
  local step = step or 1000

  for i=1,#ta,step do
    local packTail = math.min(i+step-1,#ta)
    local packSzie = packTail - i + 1
    redis.call("SUNIONSTORE",dst, dst,unpack(ta,i,packTail) )
  end
end

local function unionRedisZSet(ta,dst,step)
  local step = step or 1000

  redis.call("DEL",dst)

  for i=1,#ta,step do
    local packTail = math.min(i+step-1,#ta)
    local packSzie = packTail - i + 1
    redis.call("ZUNIONSTORE",dst,packSzie+1,dst,unpack(ta,i,packTail) )
  end
end


local function deleteMegaFromSet(ta,dst,step)
  local step = step or 1000
  
  local tp = redis.call("TYPE",dst)
  
  -- 不强制删除
  -- redis.call("DEL",dst)

  for i=1,#ta,step do
    local packTail = math.min(i+step-1,#ta)
    local packSzie = packTail - i + 1
    if tp.ok == "zset" then
      redis.call("ZREM",dst,unpack(ta,i,packTail) )
    elseif tp.ok == "set" then
      redis.call("SREM",dst,unpack(ta,i,packTail) )
    end
  end
end








local function saddTable(ta,dst,step)
  local step = step or 1000

  for i=1,#ta,step do
    local packTail = math.min(i+step-1,#ta)
    local packSzie = packTail - i + 1
    redis.call("SADD",dst,unpack(ta,i,packTail) )
  end
end

-- saddTable({1,"a",2,"b"},"ta")

local function convertToTable(t)
  local tmp = {}

  for c = 1, #t, 2 do
    tmp[t[c]] = t[c + 1]
  end
  return tmp
end


local function testWrite(n)
	redis.call("SET","test_error5",n)
    return {"success"}
end

local function testRead()
	local n = redis.call("GET","test")

    return {n}
    
end


local switch = {
  testWrite = testWrite,
  testRead = testRead,
 
  
  niltail = nil
}


local cmd = switch[KEYS[1]]
if(cmd) then
  return cmd(unpack(KEYS,2))
else
  return "no such method"
end
    
    
    
    
    --    -- rlog(result)
    --   return result
    
    -- ./redis-cli -a password --eval test.lua 
    -- redis-cli --eval ratelimiting.lua rate.limitingl:127.0.0.1 , 10 3
    -- ./redis-cli -a password  SCRIPT LOAD "$(cat test.lua)"
