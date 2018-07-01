### JAVA 中的内存泄露
Java中的内存泄露，广义并通俗的说，就是：不再会被使用的对象的内存不能被回收，就是内存泄露。在Java中，我们不用（也没办法）自己释放内存，无用的对象由GC自动清理，这也极大的简化了我们的编程工作。但，实际有时候一些不再会被使用的对象，在GC看来不能被释放，就会造成内存泄露。
### 内存泄露例子
##### 示例一
对象都是有生命周期的，有的长，有的短，如果长生命周期的对象持有短生命周期的引用，就很可能会出现内存泄露。我们举一个简单的例子
```
public class Simple {
    Object object;
    public void method1(){
        object = new Object();
        //...其他代码
    }
}
```

这里的object实例，其实我们期望它只作用于method1()方法中，且其他地方不会再用到它，但是，当method1()方法执行完成后，object对象所分配的内存不会马上被认为是可以被释放的对象，只有在Simple类创建的对象被释放后才会被释放，严格的说，这就是一种内存泄露。解决方法就是将object作为method1()方法中的局部变量。当然，如果一定要这么写，可以改为这样：
```
public class Simple {
    Object object;
    public void method1(){
        object = new Object();
        //...其他代码
        object = null;
    }
}
```
 这样，之前“new Object()”分配的内存，就可以被GC回收。

##### 示例二
Java容器ArrayList是数组实现的,如果我们要为其写一个pop()（弹出）方法，可能会是这样：
```
public E pop(){
if(size == 0)
return null;
else
return (E) elementData[--size];
}
```
写法很简洁，但这里却会造成内存溢出：elementData[size-1]依然持有E类型对象的引用，并且暂时不能被GC回收。我们可以如下修改：
```
public E pop(){
    if(size == 0)
        return null;
    else{
        E e = (E) elementData[--size];
        elementData[size] = null;
        return e;
    }
}
```
##### 示例三
```
void method(){
Vector vector = new Vector();
for (int i = 1; i<100; i++)
{
Object object = new Object();
vector.add(object);
object = null;
}
//...对vector的操作
//...与vector无关的其他操作
}
```
这里内存泄露指的是在对vector操作完成之后，执行下面与vector无关的代码时，如果发生了GC操作，这一系列的object是没法被回收的，而此处的内存泄露可能是短暂的，因为在整个method()方法执行完成后，那些对象还是可以被回收。这里要解决很简单，手动赋值为null即可：
```
void method(){
Vector vector = new Vector();
for (int i = 1; i<100; i++)
{
Object object = new Object();
vector.add(object);
object = null;
}
//...对v的操作
vector = null;
//...与v无关的其他操作
}
```